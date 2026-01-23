package app.orchestrator;

import app.conversation.ChatMessage;
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
    public String handleMessage(List<ChatMessage> conversationHistory, String userInput) {

        conversationHistory.add(ChatMessage.user(userInput));

        while (true) {
            LlmResponse response = llmClient.generateResponse(
                    conversationHistory,
                    toolRegistry.getToolSchemas()
            );

            // Case 1: LLM produced a normal response
            if (!response.hasToolCall()) {
                conversationHistory.add(
                        ChatMessage.assistant(response.getText())
                );
                return response.getText();
            }
            ToolCall toolCall = response.getToolCall();
            String toolResult = toolRegistry.execute(toolCall);

            // Feed tool result back to LLM
            conversationHistory.add(
                    ChatMessage.toolResult(
                            toolCall.name(),
                            toolResult
                    )
            );
        }
    }
}
