package app.orchestrator;

import app.conversation.Conversation;
import app.llm.LlmClient;
import app.llm.LlmResponse;
import app.llm.dtos.requests.gemini.FunctionResponse;
import app.llm.dtos.requests.gemini.Part;
import app.tools.ToolCall;
import app.tools.ToolRegistry;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
@AllArgsConstructor
@Slf4j
public class DefaultOrchestrator implements ConversationOrchestrator {
    private final LlmClient llmClient;
    private final ToolRegistry toolRegistry;

    @Override
    public String handleMessage(Conversation conversation, String userInput) {

         LlmResponse response = llmClient.generateResponse(
                conversation,
                toolRegistry
        );

        if (!response.hasToolCall()) {
            conversation.addAssistantMessage(response.getText());
            return response.getText();
        }
        List<ToolCall> toolCalls = response.getToolCalls();
        List<Part> parts = new ArrayList<>();
        for (ToolCall toolCall : toolCalls) {
            String toolResult = toolRegistry.execute(toolCall);
            FunctionResponse functionResponse = new FunctionResponse();
            functionResponse.setName(toolCall.name());
            functionResponse.setResponse(Map.of("result", toolResult));
            parts.add(new Part(functionResponse));
        }
        LlmResponse modelResponse = llmClient.generateToolResponse(conversation, parts, toolRegistry);
        conversation.addAssistantMessage(modelResponse.getText());
        return modelResponse.getText();
    }
}


