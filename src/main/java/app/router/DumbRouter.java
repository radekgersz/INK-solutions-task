package app.router;

import app.agents.AgentType;
import app.conversation.Conversation;
import org.springframework.stereotype.Component;

@Component
public class DumbRouter implements AgentRouter{

    @Override
    public AgentType route(Conversation conversation){
        return AgentType.OTHER;
    }
}

