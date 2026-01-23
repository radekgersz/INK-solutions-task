package app.orchestrator;

import app.conversation.ChatMessage;

import java.util.List;

public interface ConversationOrchestrator {
    String handleMessage(List<ChatMessage> conversationHistory, String userInput);
}
