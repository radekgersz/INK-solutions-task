package app.agents;

import app.clients.LlmClient;
import app.conversation.ChatMessage;
import app.conversation.Conversation;
import app.conversation.Role;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

import static app.agents.AgentType.OUT_OF_SCOPE;

@Component
@AllArgsConstructor
public class OutOfScopeAgent implements Agent {
    private final LlmClient llmClient;
    private static final String GENERAL_PROMPT = """
            You are an AI assistant designed to handle user queries that are outside the scope of your primary expertise.
            When a user asks a question or makes a request that does not pertain to your specialized knowledge area,
            you should respond in a polite and helpful manner, indicating that the query is outside your area of expertise.
            Never attempt to answer questions that fall outside your designated domain. Instead, respond with a message such as:
            "I'm sorry, but I am not able to assist with that request as it falls outside of my area of expertise."
            """;
    @Override
    public String respond(Conversation conversation) {
        String userText = conversation.getLastUserMessage()
                .orElseThrow()
                .content();

        List<ChatMessage> prompt = List.of(
                new ChatMessage(Role.SYSTEM, GENERAL_PROMPT),
                new ChatMessage(Role.USER, userText)
        );
        return llmClient.generateResponse(prompt);
    }

    @Override
    public AgentType type() {
        return OUT_OF_SCOPE;
    }
}
