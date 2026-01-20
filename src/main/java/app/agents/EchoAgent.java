package app.agents;

import app.conversation.ChatMessage;
import app.conversation.Conversation;

public class EchoAgent implements Agent {
    @Override
    public String respond(Conversation conversation) {
        ChatMessage last = conversation.getMessages()
                .get(conversation.getMessages().size() - 1);
        return "Echo: " + last.getContent();
    }
}


