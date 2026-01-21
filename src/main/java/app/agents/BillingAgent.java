package app.agents;

import app.billing.BillingCatalog;
import app.billing.BillingIntent;
import app.billing.Plan;
import app.clients.LlmClient;
import app.conversation.ChatMessage;
import app.conversation.Conversation;
import app.conversation.Role;
import app.properties.PromptProperties;
import org.springframework.stereotype.Component;

import java.util.List;

import static app.agents.AgentType.BILLING;
import static app.billing.BillingCatalog.formatBillingPeriod;

@Component
public class BillingAgent implements Agent {
    private final LlmClient llmClient;
    private final BillingCatalog billingCatalog;
    private final String intentPrompt;

    public BillingAgent(LlmClient llmClient, BillingCatalog billingCatalog, PromptProperties promptProperties) {
        this.llmClient = llmClient;
        this.billingCatalog = billingCatalog;
        this.intentPrompt = promptProperties.getIntent();
    }

    @Override
    public String respond(Conversation conversation) {

        String userText = conversation.getLastUserMessage()
                .orElseThrow()
                .content();

        BillingIntent intent = classify(userText);

        return switch (intent) {
            case LIST_PLANS -> handleListPlans();
            case SUBSCRIBE_PLAN -> handleSubscribe();
            case CANCEL_SUBSCRIPTION -> handleCancel();
            case OUT_OF_SCOPE ->
                    "I'm sorry, I cannot help you with that. I can only help with plans, subscriptions, or cancellations";
            case UNKNOWN -> "I can help with plans, subscriptions, or cancellations. What would you like to do?";
        };
    }

    @Override
    public AgentType type() {
        return BILLING;
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
