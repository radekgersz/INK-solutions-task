package app.agents;

import app.billing.BillingIntent;
import app.clients.LlmClient;
import app.conversation.ChatMessage;
import app.conversation.Conversation;
import app.conversation.Role;
import app.properties.AgentAnswerProperties;
import app.properties.BillingPromptProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.util.List;
import static app.agents.AgentType.BILLING;
import static app.billing.BillingCatalog.listAvailablePlans;
import static app.billing.BillingIntent.OUT_OF_SCOPE;

@Slf4j
@Component
public class BillingAgent implements Agent {
    private final LlmClient llmClient;
    private final String intentPrompt;
    private final AgentAnswerProperties agentAnswerProperties;
    private final String subscribePrompt;
    private final String cancelPrompt;
    private final String outOfScopePrompt;
    private final String listPlansPrompt;

    public BillingAgent(LlmClient llmClient, BillingPromptProperties  billingPromptProperties, AgentAnswerProperties agentAnswerProperties) {
        this.llmClient = llmClient;
        this.intentPrompt = billingPromptProperties.getIntent();
        this.agentAnswerProperties = agentAnswerProperties;
        this.subscribePrompt = null;
        this.cancelPrompt = null;
        this.outOfScopePrompt = null;
        this.listPlansPrompt = null;
    }

    @Override
    public String respond(Conversation conversation) {

        String userText = conversation.getLastUserMessage()
                .orElseThrow()
                .content();

        BillingIntent intent = classify(userText);

        return switch (intent) {
            case LIST_PLANS -> handleListPlans(conversation);
            case SUBSCRIBE_PLAN -> handleSubscribe(conversation);
            case CANCEL_SUBSCRIPTION -> handleCancel(conversation);
            case OUT_OF_SCOPE -> handleOutOfScope(conversation);
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
            return OUT_OF_SCOPE;
        }

        String normalized = raw.trim()
                .toUpperCase()
                .replaceAll("[^A-Z_]", "");

        try {
            return BillingIntent.valueOf(normalized);
        } catch (IllegalArgumentException e) {
            log.warn("Unrecognized billing intent: '{}'", raw);
            return OUT_OF_SCOPE;
        }
    }
    private String handleCancel(Conversation conversation) {
        return respondAccordingly(conversation, cancelPrompt);
    }
    private String handleSubscribe(Conversation conversation) {
        return respondAccordingly(conversation, subscribePrompt);
    }
    private String handleOutOfScope(Conversation conversation) {
        return  respondAccordingly(conversation, outOfScopePrompt);
    }

    private String handleListPlans(Conversation conversation) {
        return respondAccordingly(conversation, listPlansPrompt);
    }

    private String respondAccordingly(Conversation conversation, String prompt){
        return "";
    }
    @Override
    public AgentType type() {
        return BILLING;
    }


}
