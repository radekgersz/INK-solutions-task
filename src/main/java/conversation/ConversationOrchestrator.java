package conversation;

import java.util.UUID;

public interface ConversationOrchestrator {
    String handleUserMessage(UUID conversationId, String userMessage);
}
