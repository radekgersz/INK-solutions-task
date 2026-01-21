package app.agents;

import app.clients.LlmClient;
import app.conversation.ChatMessage;
import app.conversation.Conversation;
import app.conversation.Role;
import app.properties.PromptProperties;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

import static app.agents.AgentType.OUT_OF_SCOPE;

@Component
public class OutOfScopeAgent implements Agent {
    private final LlmClient llmClient;
    private final String outOfScope;

    public OutOfScopeAgent(LlmClient llmClient, PromptProperties promptProperties) {
        this.llmClient = llmClient;
        this.outOfScope = promptProperties.getOutOfScope();
    }
    public String respond(Conversation conversation) {
        String userText = conversation.getLastUserMessage()
                .orElseThrow()
                .content();

        List<ChatMessage> prompt = List.of(
                new ChatMessage(Role.SYSTEM, outOfScope),
                new ChatMessage(Role.USER, userText)
        );
        return llmClient.generateResponse(prompt);
    }

    @Override
    public AgentType type() {
        return OUT_OF_SCOPE;
    }
}
