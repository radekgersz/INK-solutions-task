package app.billing;

import app.clients.LlmClient;
import app.conversation.ChatMessage;
import app.conversation.Role;
import app.properties.PromptProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BillingIntentClassifier {

    private final String intentPrompt;

    private final LlmClient llmClient;

    public BillingIntentClassifier(LlmClient llmClient, PromptProperties promptProperties) {
        this.llmClient = llmClient;
        this.intentPrompt = promptProperties.getIntent();
    }

    public BillingIntent classify(String userMessage) {

        List<ChatMessage> prompt = List.of(
                new ChatMessage(Role.SYSTEM, intentPrompt),
                new ChatMessage(Role.USER, userMessage)
        );

        String raw = llmClient.generateResponse(prompt);

        return parse(raw);
    }

    private BillingIntent parse(String raw) {
        if (raw == null) {
            return BillingIntent.UNKNOWN;
        }

        String normalized = raw.trim()
                .toUpperCase()
                .replaceAll("[^A-Z_]", "");

        try {
            return BillingIntent.valueOf(normalized);
        } catch (IllegalArgumentException e) {
            return BillingIntent.UNKNOWN;
        }
    }
}
