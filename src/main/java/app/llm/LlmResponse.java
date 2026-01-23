package app.llm;

import app.tools.ToolCall;
import lombok.Getter;

@Getter
public class LlmResponse {

    private final String text;
    private final ToolCall toolCall;

    private LlmResponse(String text, ToolCall toolCall) {
        this.text = text;
        this.toolCall = toolCall;
    }

    public static LlmResponse text(String text) {
        return new LlmResponse(text, null);
    }

    public static LlmResponse toolCall(ToolCall call) {
        return new LlmResponse(null, call);
    }

    public boolean hasToolCall() {
        return toolCall != null;
    }
}
