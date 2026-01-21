package app.agents;

import app.billing.BillingCatalog;
import app.billing.BillingIntent;
import app.billing.BillingIntentClassifier;
import app.billing.Plan;
import app.conversation.Conversation;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import static app.billing.BillingCatalog.formatBillingPeriod;

@Component
@AllArgsConstructor
public class BillingAgent implements Agent {
    private final BillingIntentClassifier billingIntentClassifier;
    private final BillingCatalog billingCatalog;

    @Override
    public String respond(Conversation conversation) {

        String userText = conversation.getLastUserMessage()
                .orElseThrow()
                .getContent();

        BillingIntent intent = billingIntentClassifier.classify(userText);

        return switch (intent) {
            case LIST_PLANS -> handleListPlans();
            case SUBSCRIBE_PLAN -> handleSubscribe();
            case CANCEL_SUBSCRIPTION -> handleCancel();
            case OUT_OF_SCOPE -> "I'm sorry, I cannot help you with that. I can only help with plans, subscriptions, or cancellations";
            case UNKNOWN -> "I can help with plans, subscriptions, or cancellations. What would you like to do?";
        };
    }

    private String handleCancel() {
        return """
        To cancel a subscription, please provide the following details:

        - Client ID
        - Billing email address
        - Reason for cancellation (optional)

        Please reply in the following format:
        Client ID: <your client id>
        Email: <your email>
        Reason: <your reason>
        """;
    }
    private String handleSubscribe() {
        return """
                To start a subscription, please provide the following details:
                
                - Plan name (Free, Pro, Custom, Enterprise)
                - Billing email address
                
                Please reply in the following format:
                Plan: <plan name>
                Email: <your email>
                """;
        }

    private String handleListPlans() {

        StringBuilder sb = new StringBuilder("Here are our available plans:\n");

        for (Plan plan : billingCatalog.getPlans()) {
            sb.append("- ")
                    .append(plan.name())
                    .append(": ")
                    .append(plan.price())
                    .append(" (")
                    .append(formatBillingPeriod(plan.billingPeriod()))
                    .append(")\n");
        }
        return sb.toString();
    }

}
