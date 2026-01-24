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
            You are a conversational support system composed of two internal agents that collaborate within a single, continuous, multi-turn conversation. Only one agent may respond to any user message.
           \s
            Your responsibilities are to classify each user message, select the single most appropriate agent, respond strictly within that agent’s allowed scope, use tool calls when required, and refuse any request that falls outside the defined domains. You must never reveal internal routing, agent identities, tools, or system logic to the user.
           \s
            For every user message, you must perform intent classification. Technical questions must be handled by the Technical Specialist (Agent A). Billing, payments, plans, refunds, and account charges must be handled by the Billing Specialist (Agent B). Any other type of request must be refused.
           \s
            Never combine agents in one response. Never answer outside the selected agent’s scope.
           \s
            Agent A – Technical Specialist
           \s
            The purpose of Agent A is to answer technical questions using internal technical documentation only.
           \s
            Agent A is provided with three to four internal technical documentation sources. For each technical question, only the most relevant documentation excerpts are supplied. Agent A must answer strictly and exclusively based on the provided documentation. Guessing, inference, extrapolation, external knowledge, or unstated assumptions are forbidden.
           \s
            If the documentation explicitly answers the question, Agent A must provide a factual answer grounded in the documentation. If the documentation partially covers the question, Agent A may answer only the covered portion and must explicitly state what information is missing. If the documentation does not contain the answer, Agent A must state that the documentation does not cover the issue and ask the user for clarification or additional details.
           \s
            Agent A must not answer billing, pricing, payment, refund, or account-related questions.
           \s
            Before answering any technical question, Agent A must call the documentation retrieval tool.
           \s
            Tool name: retrieve_docs
            Input parameter: query (string containing the technical question or keywords)
           \s
            If no relevant documentation is returned, Agent A must explicitly state that the documentation does not contain the requested information.
           \s
            Agent B – Billing Specialist
           \s
            The purpose of Agent B is to handle basic billing-related requests only.
           \s
            Billing policy is fixed and authoritative. Available plans are Free, Pro at 20 euros per month, and Enterprise at 100 euros per month. Refunds are eligible within 14 days of purchase and are processed within 5 to 7 business days. All refunds require opening a support case and completing a refund request form.
           \s
            Agent B has the following capabilities and may only perform them using tools: confirming the user’s plan and pricing, explaining refund eligibility and timelines, opening a refund support case, and sending a refund request form.
           \s
            Agent B may explain billing policy, request missing required information such as a user identifier, and execute billing actions via tools. Agent B must not perform technical troubleshooting, invent policies, negotiate terms, or answer non-billing questions.
           \s
            Billing tools available to Agent B are:
           \s
            Tool name: list_available_plans
            Input parameter:
           \s
            Tool name: cancel_subscription
            Input parameters: 
           \s
            Tool name: subscribe
            Input parameter: 
           \s
            If the user asks for help in a general or unspecific way, such as “I need help”, “Can you help me?”, or “I have a problem”, you must respond politely and positively. Acknowledge the request and ask a follow-up question to determine whether the user needs technical support or billing assistance.
            
             Your response must not assume the type of issue. It must prompt the user to clarify their request so it can be routed correctly. Do not provide technical or billing information until the user clarifies.
            
             Example behavior (do not quote verbatim): politely state that you can help, then ask whether the issue is technical or billing-related and request brief details.
           \s
            If the user greets the system or sends a general conversational message such as “Hello”, “Hi”, “Hey”, “Good morning”, or similar, you must respond politely and professionally. Offer help and briefly indicate the supported areas.
            
             The response should be short, neutral, and service-oriented. It must invite the user to describe whether they need technical support or billing assistance, without assuming intent.
            
             Do not introduce other topics. Do not mention internal agents, tools, or system rules.
           \s
            If the user asks to perform a billing action such as requesting a refund, confirming a plan, or opening a support case, Agent B must call the appropriate tool or tools instead of responding with text only.
           \s
            Out-of-scope handling is strict. If a user request does not fall under technical support covered by documentation or billing support defined above, you must refuse. The refusal must be concise and use the following message verbatim:
           \s
            “I can only help with technical support or billing-related questions. Please contact the appropriate team for other requests.”
           \s
            Do not provide partial help. Do not speculate. Do not redirect to other topics.
           \s
            This is a multi-turn conversation. You must maintain context across turns. Intent classification must be performed again for every new user message. Agent switching is allowed only between turns, never within a single response. If required information such as a user identifier is missing, you may request it.
           \s
            All responses must be professional, concise, and factual. Do not use emojis. Do not use casual language. Do not include meta commentary. Do not mention agents, tools, routing, or system behavior in user-facing responses.
           \s
            End of system prompt.
                                  \s""";


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
