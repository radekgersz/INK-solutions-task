package app.llm;

import app.conversation.ChatMessage;
import app.conversation.Role;
import app.llm.dtos.requests.gemini.*;
import app.llm.dtos.responses.gemini.ContentResponseDTO;
import app.llm.dtos.responses.gemini.FunctionCallResponseDTO;
import app.llm.dtos.responses.gemini.PartResponseDTO;
import app.llm.dtos.responses.gemini.ResponseDTO;
import app.tools.Tool;
import app.tools.ToolArgument;
import app.tools.ToolCall;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static app.conversation.Role.SYSTEM;

public class GeminiParser {
    private static String prompt = """
            You are an assistant that can either:
            1) respond normally in natural language, or
2) return a tool call in structured JSON.

    IMPORTANT RULE:
    Only return a tool call if the user message is CLEARLY, DIRECTLY, and EXPLICITLY requesting an action that matches the tool’s purpose.

    Do NOT return a tool call if:
            - the user is asking a general question
- the user is discussing a topic conceptually
- the user is unsure, speculative, or exploratory
- the user mentions the tool or its domain casually
- the request could reasonably be answered with text alone

    When in doubt, ALWAYS respond with normal text.

    Tool calls must be used only when:
            - the user intent is unambiguous
- the request cannot be fulfilled without calling the tool
- the request directly maps to the tool’s inputs and outputs

    If the request is ambiguous or incomplete, ask a clarification question instead of calling the tool.

    Output format:
            - If calling a tool: return ONLY the valid tool-call JSON, with no extra text.
- Otherwise: respond normally in plain text.
""";


    public static LlmResponse parseResponse(ResponseDTO dto) {
        if (dto == null || dto.getCandidates() == null || dto.getCandidates().isEmpty()) {
            return new LlmResponse("", List.of());
        }

        ContentResponseDTO content = dto.getCandidates().getFirst().getContent();

        if (content == null || content.getParts() == null) {
            return new LlmResponse("", List.of());
        }

        StringBuilder textBuilder = new StringBuilder();
        List<ToolCall> toolCalls = new ArrayList<>();

        for (PartResponseDTO part : content.getParts()) {
            if (part == null) {
                continue;
            }

            // Collect text
            if (part.getText() != null) {
                textBuilder.append(part.getText());
            }

            // Collect function calls
            if (part.getFunctionCall() != null) {
                toolCalls.add(mapToToolCall(part.getFunctionCall()));
            }
        }

        return new LlmResponse(
                textBuilder.toString(),
                List.copyOf(toolCalls)
        );
    }
    public static ToolCall mapToToolCall(FunctionCallResponseDTO dto) {
        return new ToolCall(
                dto.getName(),
                dto.getArgs()
        );
    }
    public static RequestDTO createRequest(
            List<ChatMessage> messages,
            Map<String, Tool> tools
    ) {
        List<Content> contents = Stream.concat(
                Stream.of(
                        new Content(
                                SYSTEM.name(),
                                List.of(new Part(prompt))
                        )
                ),
                messages.stream()
                        .map(msg -> new Content(
                                msg.role().name(),
                                List.of(new Part(msg.content()))
                        ))
        ).toList();

        List<FunctionDeclaration> functionDeclarations =
                tools.values().stream()
                        .map(GeminiParser::mapToolToFunctionDeclaration)
                        .toList();

        ToolRequestDTO toolRequestDTO = new ToolRequestDTO();
        toolRequestDTO.setFunctionDeclarations(functionDeclarations);


        return new RequestDTO(
                contents,
                List.of(toolRequestDTO)
        );
    }

    public static FunctionDeclaration mapToolToFunctionDeclaration(Tool tool) {
        FunctionDeclaration dto = new FunctionDeclaration();
        dto.setName(tool.name());
        dto.setDescription(tool.description());
        dto.setParameters(mapParameters(tool.arguments()));
        return dto;
    }
    public static Parameters mapParameters(List<ToolArgument> arguments) {

        Map<String, Property> properties = new HashMap<>();
        List<String> required = new ArrayList<>();

        for (ToolArgument arg : arguments) {
            Property prop = new Property();
            prop.setType(arg.getType()); // "string", "number", etc.
            prop.setDescription(arg.getDescription());

            properties.put(arg.getName(), prop);

            if (arg.isRequired()) {
                required.add(arg.getName());
            }
        }

        Parameters params = new Parameters();
        params.setType("object");
        params.setProperties(properties);
        params.setRequired(required);
        return params;
    }
}
