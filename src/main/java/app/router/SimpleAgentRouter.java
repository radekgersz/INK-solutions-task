package app.router;

import app.agents.AgentType;
import app.conversation.ChatMessage;
import app.conversation.Conversation;
import app.conversation.Role;
import app.clients.LlmClient;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SimpleAgentRouter implements AgentRouter {

    private static final String ROUTING_PROMPT = """
        You are a classifier that decides which support agent should handle a user request.

        Agent types:
        - TECHNICAL: product usage, errors, APIs, integrations
        - BILLING: pricing, plans, payments, refunds
        - OUT_OF_SCOPE: anything else

        Return ONLY one of:
        TECHNICAL
        BILLING
        OUT_OF_SCOPE
        
        Do not justify your response.
        """;

    private final LlmClient llmClient;

    public SimpleAgentRouter(LlmClient llmClient) {
        this.llmClient = llmClient;
    }

    @Override
    public AgentType route(Conversation conversation) {

        ChatMessage lastUserMessage = null;
        List<ChatMessage> messages = conversation.getMessages();

        for (int i = messages.size() - 1; i >= 0; i--) {
            ChatMessage m = messages.get(i);
            if (m.role() == Role.USER) {
                lastUserMessage = m;
                break;
            }
        }

        if (lastUserMessage == null) {
            throw new IllegalStateException("No user message found");
        }
        List<ChatMessage> prompt = List.of(
                new ChatMessage(Role.SYSTEM, ROUTING_PROMPT),
                lastUserMessage
        );

        String raw = llmClient.generateResponse(prompt);

        String normalized = raw.trim()
                .toUpperCase()
                .replaceAll("[^A-Z_]", "");

        return AgentType.valueOf(normalized);
    }
}

