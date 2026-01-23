package app.orchestrator;

import app.conversation.ChatMessage;
import app.conversation.Conversation;
import app.llm.LlmClient;
import app.llm.LlmResponse;
import app.tools.ToolCall;
import app.tools.ToolRegistry;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class DefaultOrchestrator implements ConversationOrchestrator {
    private final LlmClient llmClient;
    private final ToolRegistry toolRegistry;

    @Override
    public String handleMessage(Conversation conversation, String userInput) {

        //make an initial user message, potentially a tool call
         LlmResponse response = llmClient.generateResponse(
                conversation,
                toolRegistry.getToolSchemas()
        );

        if (!response.hasToolCall()) {
            conversation.addAssistantMessage(userInput);
            return response.getText();
        }
        List<ToolCall> toolCalls = response.getToolCalls();
//        String toolResult = toolRegistry.execute(toolCall);
//
//        conversationHistory.add(
//                ChatMessage.toolResult(
//                        toolCall.name(),
//                        toolResult
//                )
//        );
        return "";
    }
}


