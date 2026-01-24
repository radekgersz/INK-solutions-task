package app.orchestrator;

import app.conversation.ChatMessage;
import app.conversation.Conversation;
import app.llm.LlmClient;
import app.llm.LlmResponse;
import app.tools.ToolCall;
import app.tools.ToolRegistry;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
@Slf4j
public class DefaultOrchestrator implements ConversationOrchestrator {
    private final LlmClient llmClient;
    private final ToolRegistry toolRegistry;

    @Override
    public String handleMessage(Conversation conversation, String userInput) {

        //make an initial user message, potentially a tool call
         LlmResponse response = llmClient.generateResponse(
                conversation,
                toolRegistry
        );

        if (!response.hasToolCall()) {
            conversation.addAssistantMessage(userInput);
            return response.getText();
        }
        List<ToolCall> toolCalls = response.getToolCalls();
        for (ToolCall toolCall : toolCalls) {
            String toolResult = toolRegistry.execute(toolCall);
            log.info(toolResult);
        }
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


