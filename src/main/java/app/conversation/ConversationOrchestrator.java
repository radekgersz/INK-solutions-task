package app.conversation;

import org.springframework.stereotype.Component;

import java.util.UUID;

public interface ConversationOrchestrator {
    String handleUserMessage(UUID conversationId, String userMessage);
}
