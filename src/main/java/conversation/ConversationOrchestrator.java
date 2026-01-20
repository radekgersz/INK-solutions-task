package conversation;

public interface ConversationOrchestrator {
    String handleUserMessage(int conversationId, String userMessage);
}
