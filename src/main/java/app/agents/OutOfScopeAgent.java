package app.agents;

import app.clients.LlmClient;
import app.conversation.ChatMessage;
import app.conversation.Conversation;
import app.conversation.Role;
import app.properties.OutOfScopePromptProperties;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

import static app.agents.AgentType.OUT_OF_SCOPE;

@Component
public class OutOfScopeAgent implements Agent {
    private final LlmClient llmClient;
    private final String outOfScopePrompt;
    private static final int NUM_MESSAGES = 3;

    public OutOfScopeAgent(LlmClient llmClient, OutOfScopePromptProperties outOfScopePromptProperties) {
        this.llmClient = llmClient;
        this.outOfScopePrompt = outOfScopePromptProperties.getOutOfScopePrompt();
    }
    public String respond(Conversation conversation) {
        List<ChatMessage> chatMessages = conversation.getLastNMessages(NUM_MESSAGES);
        LinkedList<ChatMessage> prompt = new LinkedList<>(chatMessages);
        prompt.addFirst(new ChatMessage(Role.SYSTEM, outOfScopePrompt));
        return llmClient.generateResponse(prompt);
    }

    @Override
    public AgentType type() {
        return OUT_OF_SCOPE;
    }
}
