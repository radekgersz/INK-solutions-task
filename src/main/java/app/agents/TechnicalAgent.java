package app.agents;

import app.conversation.Conversation;
import org.springframework.stereotype.Component;

@Component
public class TechnicalAgent implements Agent{
    @Override
    public String respond(Conversation conversation) {
        return "";
    }
}
