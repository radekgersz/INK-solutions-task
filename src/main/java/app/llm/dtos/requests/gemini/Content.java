package app.llm.dtos.requests.gemini;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Content {

    private String role;
    private List<Part> parts;

    public Content() {}

    public Content(String role, List<Part> parts) {
        this.role = role;
        this.parts = parts;
    }
}

