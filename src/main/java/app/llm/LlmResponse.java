package app.llm;

import app.tools.ToolCall;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class LlmResponse {

    private final String text;
    private final List<ToolCall> toolCalls;

    public LlmResponse(String text, List<ToolCall> toolCalls) {
        this.text = text;
        this.toolCalls = toolCalls;
    }

    public boolean hasToolCall() {
        return !toolCalls.isEmpty();
    }
}
