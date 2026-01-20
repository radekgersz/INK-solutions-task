package conversation;

import agents.Agent;
import agents.AgentType;
import lombok.AllArgsConstructor;
import router.AgentRouter;

import java.util.Map;
import java.util.UUID;

@AllArgsConstructor
public class DefaultOrchestrator implements ConversationOrchestrator {

    private final ConversationMemory memory;
    private final AgentRouter router;
    private final Map<AgentType, Agent> agents;


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
