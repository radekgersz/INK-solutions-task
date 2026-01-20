package app.router;

import app.agents.AgentType;
import app.conversation.Conversation;

public class DumbRouter implements AgentRouter{

    @Override
    public AgentType route(Conversation conversation){
        return AgentType.OTHER;
    }
}
