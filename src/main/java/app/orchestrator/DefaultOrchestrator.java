package app.orchestrator;

import app.agents.Agent;
import app.agents.AgentType;
import app.conversation.Conversation;
import app.conversation.ConversationMemory;
import app.router.AgentRouter;
import org.springframework.stereotype.Component;
import java.util.Map;
import java.util.UUID;

@Component
public class DefaultOrchestrator implements ConversationOrchestrator {

    private final ConversationMemory memory;
    private final AgentRouter router;
    private final Map<AgentType,Agent> agents;


    public DefaultOrchestrator(ConversationMemory memory, AgentRouter router, Map<AgentType, Agent> agents) {
        this.memory = memory;
        this.router = router;
        this.agents = agents;

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
