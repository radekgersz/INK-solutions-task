package app.llm;

import app.conversation.ChatMessage;
import app.conversation.Conversation;
import app.tools.ToolRegistry;


import java.util.List;

public interface LlmClient {


    LlmResponse generateResponse(Conversation conversation, ToolRegistry toolRegistry);
}
