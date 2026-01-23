package app.llm.dtos.requests.gemini;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FunctionDeclaration {

    private String name;
    private String description;
    private Parameters parameters;

    public FunctionDeclaration() {}

    public FunctionDeclaration(String name, String description, Parameters parameters) {
        this.name = name;
        this.description = description;
        this.parameters = parameters;
    }
}

