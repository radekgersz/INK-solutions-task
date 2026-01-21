package app.billing;

import app.clients.LlmClient;
import app.conversation.ChatMessage;
import app.conversation.Role;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BillingIntentClassifier {

    private static final String INTENT_PROMPT = """
        You are a billing intent classifier.

        Possible intents:
        - LIST_PLANS: user wants to see available plans or pricing
        - SUBSCRIBE_PLAN: user wants to start or change a subscription
        - CANCEL_SUBSCRIPTION: user wants to cancel or stop a subscription
        - OUT_OF_SCOPE: user is asking something related to billing but not covered by the above intents
        - UNKNOWN: anything else

        Return ONLY one of:
        LIST_PLANS
        SUBSCRIBE_PLAN
        CANCEL_SUBSCRIPTION
        OUT_OF_SCOPE
        UNKNOWN

        Do not explain your answer.
        """;

    private final LlmClient llmClient;

    public BillingIntentClassifier(LlmClient llmClient) {
        this.llmClient = llmClient;
    }

    public BillingIntent classify(String userMessage) {

        List<ChatMessage> prompt = List.of(
                new ChatMessage(Role.SYSTEM, INTENT_PROMPT),
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
