package app.conversation;

import org.springframework.stereotype.Component;

import java.util.UUID;


public class DummyOrchestrator implements ConversationOrchestrator {

    @Override
    public String handleUserMessage(UUID conversationId, String userMessage) {
        return "Echo: " + userMessage;
    }
}