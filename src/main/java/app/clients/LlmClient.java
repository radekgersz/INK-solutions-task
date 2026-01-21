package app.clients;

import app.conversation.ChatMessage;

import java.util.List;

public interface LlmClient {


    String generateResponse(List<ChatMessage> messages);
}
