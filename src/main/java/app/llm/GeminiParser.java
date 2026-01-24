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
import static app.conversation.Role.USER;

public class GeminiParser {
    private static String prompt = """
            You are an AI assistant designed to help with billing or technical tasks.
            
            You will be provided with available tools to assist you in completing these tasks.
            
            Only use the tools that clearly fit the user's request, eg. if the user asks for available plans, use the "get_available_plans" tool.
            
            If the user request is unrelated to billing or technical tasks, respond that you are unable to assist with that request.
            
            If the user generally asks for help, respond politely and ask them to specify if they need billing or technical assistance.
            
            If the user says things like hello, hi, thanks, etc., respond politely without using any tools.
            
            You will be provided with previous that may help you assist the user. Only use the previous messages if you are unable to fulfill the request in an unambiguous way.
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
    public static RequestDTO createToolRequest(
            List<ChatMessage> messages,
            Map<String, Tool> tools,
            List<Part> parts
    ) {
        List<Content> contents = Stream.concat(
                Stream.concat(
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
                ),
                Stream.of(
                        new Content(
                                USER.name(),
                                parts
                        )
                )
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
            prop.setType(arg.getType());
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
