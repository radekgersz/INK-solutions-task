package app.llm.dtos.requests.gemini;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Property {

    private String type;
    private String description;

    public Property() {}

    public Property(String type, String description) {
        this.type = type;
        this.description = description;
    }
}

