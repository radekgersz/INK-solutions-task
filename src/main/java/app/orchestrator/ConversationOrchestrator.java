package app.orchestrator;

import app.conversation.ChatMessage;
import app.conversation.Conversation;

import java.util.List;
import java.util.UUID;

public interface ConversationOrchestrator {
    String handleMessage(Conversation conversation, String userInput);
}
