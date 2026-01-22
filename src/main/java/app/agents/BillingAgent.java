package app.agents;

import app.billing.BillingIntent;
import app.clients.LlmClient;
import app.conversation.ChatMessage;
import app.conversation.Conversation;
import app.properties.AgentAnswerProperties;
import app.properties.BillingPromptProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

import static app.agents.AgentType.BILLING;
import static app.billing.BillingCatalog.listAvailablePlans;
import static app.billing.BillingIntent.OUT_OF_SCOPE;
import static app.conversation.Role.SYSTEM;
import static app.conversation.Role.USER;

@Slf4j
@Component
public class BillingAgent implements Agent {
    private final LlmClient llmClient;
    private final String intentPrompt;
    private final AgentAnswerProperties agentAnswerProperties;
    private final String outOfScopePrompt;
    private static final int numMessages = 3;

    public BillingAgent(LlmClient llmClient, BillingPromptProperties  billingPromptProperties, AgentAnswerProperties agentAnswerProperties) {
        this.llmClient = llmClient;
        this.intentPrompt = billingPromptProperties.getIntentPrompt();
        this.agentAnswerProperties = agentAnswerProperties;
        this.outOfScopePrompt = billingPromptProperties.getBillingOutOfScopePrompt();
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
            case OUT_OF_SCOPE -> handleOutOfScope(conversation);
        };
    }


    public BillingIntent classify(String userMessage) {

        List<ChatMessage> prompt = List.of(
                new ChatMessage(SYSTEM, intentPrompt),
                new ChatMessage(USER, userMessage)
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
    private String handleCancel() {
        return agentAnswerProperties.getCancelSubscriptionMessage();
    }
    private String handleSubscribe() {
        return agentAnswerProperties.getSubscribeMessage();
    }
    private String handleOutOfScope(Conversation conversation) {
        List<ChatMessage> recentMessages = conversation
                .getLastNMessages(numMessages);
        LinkedList<ChatMessage> prompt = new LinkedList<>(recentMessages);
        prompt.addFirst(new ChatMessage(SYSTEM, outOfScopePrompt));
        return llmClient.generateResponse(prompt);
    }

    private String handleListPlans() {
        return listAvailablePlans();
    }
    @Override
    public AgentType type() {
        return BILLING;
    }


}
