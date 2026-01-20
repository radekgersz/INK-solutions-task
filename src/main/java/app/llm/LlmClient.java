package app.llm;

import app.conversation.ChatMessage;

import java.util.List;

public interface LlmClient {


    String classifyMessage(List<ChatMessage> messages);
}
