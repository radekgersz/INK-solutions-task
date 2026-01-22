package app.agents;

import app.billing.BillingIntent;
import app.clients.LlmClient;
import app.conversation.ChatMessage;
import app.conversation.Conversation;
import app.conversation.Role;
import app.properties.AgentAnswerProperties;
import app.properties.PromptProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.util.List;
import static app.agents.AgentType.BILLING;
import static app.billing.BillingCatalog.listAvailablePlans;

@Slf4j
@Component
public class BillingAgent implements Agent {
    private final LlmClient llmClient;
    private final String intentPrompt;
    private final AgentAnswerProperties agentAnswerProperties;

    public BillingAgent(LlmClient llmClient, PromptProperties promptProperties, AgentAnswerProperties agentAnswerProperties) {
        this.llmClient = llmClient;
        this.intentPrompt = promptProperties.getIntent();
        this.agentAnswerProperties = agentAnswerProperties;
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
            case OUT_OF_SCOPE -> handleOutOfScope();
            case UNKNOWN -> handleUnknown();
        };
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
            log.warn("Unrecognized billing intent: '{}'", raw);
            return BillingIntent.UNKNOWN;
        }
    }
    private String handleCancel() {
        return agentAnswerProperties.getCancelSubscriptionMessage();
    }
    private String handleSubscribe() {
        return agentAnswerProperties.getSubscribeMessage();
    }
    private String handleOutOfScope() {
        return agentAnswerProperties.getOutOfScopeMessage();
    }
    private String handleUnknown() {
        return agentAnswerProperties.getUnknownMessage();
    }

    private String handleListPlans() {
        return listAvailablePlans();
    }
    @Override
    public AgentType type() {
        return BILLING;
    }

}
