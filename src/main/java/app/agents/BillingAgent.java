package app.agents;

import app.billing.BillingIntent;
import app.billing.BillingIntentClassifier;
import app.clients.LlmClient;
import app.conversation.Conversation;
import app.billing.BillingCatalog;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class BillingAgent implements Agent {
    private final BillingIntentClassifier billingIntentClassifier;

    @Override
    public String respond(Conversation conversation) {

        String userText = conversation.getLastUserMessage()
                .orElseThrow()
                .getContent();

        BillingIntent intent = billingIntentClassifier.classify(userText);

        return switch (intent) {
            case LIST_PLANS -> "handleListPlans()";
            case SUBSCRIBE_PLAN -> "handleSubscribe(userText)";
            case CANCEL_SUBSCRIPTION -> "handleCancel(userText)";
            case UNKNOWN -> "I can help with plans, subscriptions, or cancellations. What would you like to do?";
        };
    }
}
