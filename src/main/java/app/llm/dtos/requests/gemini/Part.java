package app.llm.dtos.requests.gemini;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Part {

    private String text;

    public Part() {}

    public Part(String text) {
        this.text = text;
    }
}

