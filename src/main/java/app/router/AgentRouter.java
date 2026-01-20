package app.router;

import app.agents.AgentType;
import app.conversation.Conversation;

public interface AgentRouter {
    public AgentType route(Conversation conversation);
}
