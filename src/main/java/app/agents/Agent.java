package app.agents;

import app.conversation.Conversation;

public interface Agent {
    public String respond(Conversation conversation);
}
