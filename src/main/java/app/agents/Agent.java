package app.agents;

import app.conversation.Conversation;
import org.springframework.stereotype.Component;

@Component
public interface Agent {
    String respond(Conversation conversation);
    AgentType type();
}
