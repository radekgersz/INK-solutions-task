package app.router;

import app.agents.AgentType;
import app.conversation.ChatMessage;
import app.conversation.Conversation;
import app.conversation.Role;
import app.clients.LlmClient;
import app.properties.PromptProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SimpleAgentRouter implements AgentRouter {

    private final String routingPrompt;
    private final LlmClient llmClient;
    private final String initialPrompt;
    private static final int numMessages = 3;

    public SimpleAgentRouter(LlmClient llmClient, PromptProperties promptProperties) {
        this.llmClient = llmClient;
        this.routingPrompt = promptProperties.getRouting();
        this.initialPrompt = promptProperties.getInitialPrompt();
    }

    @Override
    public AgentType route(Conversation conversation) {
        int userMessageCount = conversation.getUserMessageCount();
        if (userMessageCount == 0) {
            throw new IllegalStateException("Conversation has no user messages");
        }
        if (userMessageCount == 1) {
            return routeFirst(conversation);
        }
        return routeSubsequent(conversation);
    }

    private AgentType routeSubsequent(Conversation conversation) {
        List<ChatMessage> recentMessages = conversation
                .getLastNMessages(numMessages);
        List<ChatMessage> prompt = recentMessages.stream()
                .toList();
        prompt.addFirst(new ChatMessage(Role.SYSTEM, routingPrompt));
        return generateAndNormalize(prompt);
    }

    private AgentType routeFirst(Conversation conversation) {
        ChatMessage userMessage = conversation
                .getLastUserMessage()
                .orElseThrow();
        List<ChatMessage> prompt = List.of(
                new ChatMessage(Role.SYSTEM, initialPrompt),
                userMessage

        );
        return generateAndNormalize(prompt);
    }
    private AgentType generateAndNormalize(List<ChatMessage> prompt) {
        String raw = llmClient.generateResponse(prompt);
        String normalized = raw.trim()
                .toUpperCase()
                .replaceAll("[^A-Z_]", "");
        return AgentType.valueOf(normalized);
    }
}

