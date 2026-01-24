package app.llm.dtos.requests.gemini;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Part {

    private String text;
    private FunctionResponse functionResponse;

    public Part(String text) {
        this.text = text;
    }
    public Part(FunctionResponse functionResponse) {
        this.functionResponse = functionResponse;
    }
}

