package app.agents;

import app.conversation.Conversation;
import org.springframework.stereotype.Component;

@Component
public interface Agent {
    public String respond(Conversation conversation);
}
