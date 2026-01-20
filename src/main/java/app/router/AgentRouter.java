package app.router;

import app.agents.AgentType;
import app.conversation.Conversation;
import org.springframework.stereotype.Component;


public interface AgentRouter {
    AgentType route(Conversation conversation);
}
