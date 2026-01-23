package app.llm;

import app.conversation.ChatMessage;
import app.tools.ToolSchema;

import java.util.List;

public interface LlmClient {


    String generateResponse(List<ChatMessage> messages, List<ToolSchema> toolSchemas);
}
