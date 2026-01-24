package app.llm;

import app.conversation.ChatMessage;
import app.conversation.Conversation;
import app.llm.dtos.requests.gemini.Part;
import app.tools.ToolRegistry;


import java.util.List;

public interface LlmClient {


    LlmResponse generateResponse(Conversation conversation, ToolRegistry toolRegistry);

    LlmResponse generateToolResponse(Conversation conversation, List<Part> parts, ToolRegistry toolRegistry);
}
