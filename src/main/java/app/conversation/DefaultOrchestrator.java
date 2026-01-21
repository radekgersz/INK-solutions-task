package app.conversation;

import app.agents.Agent;
import app.agents.AgentType;
import app.agents.EchoAgent;
import org.springframework.stereotype.Component;
import app.router.AgentRouter;

import java.util.Map;
import java.util.UUID;

@Component
public class DefaultOrchestrator implements ConversationOrchestrator {

    private final ConversationMemory memory;
    private final AgentRouter router;
    private final Map<AgentType, Agent> agents;


    public DefaultOrchestrator(ConversationMemory memory, AgentRouter router, EchoAgent echoAgent){
        this.memory = memory;
        this.router = router;
        this.agents = Map.of(
                AgentType.OUT_OF_SCOPE, echoAgent
        );
    }

    @Override
    public String handleUserMessage(UUID conversationId, String userMessage) {
        Conversation conversation = memory.getOrCreateConversation(conversationId);
        conversation.addUserMessage(userMessage);
        AgentType agentType = router.route(conversation);
        Agent agent = agents.get(agentType);
        String response = agent.respond(conversation);
        conversation.addAssistantMessage(response);
        // normally you would save the conversation, but we don't persist them here
        return response;
    }
}
