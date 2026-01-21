package app.agents;

import app.conversation.ChatMessage;
import app.conversation.Conversation;
import org.springframework.stereotype.Component;


@Component
public class EchoAgent implements Agent {
    @Override
    public String respond(Conversation conversation) {
        ChatMessage last = conversation.getMessages()
                .getLast();
        return "Echo: " + last.getContent();
    }
}


