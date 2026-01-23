package app.llm.dtos.requests.gemini;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ToolDTO {

    private List<FunctionDeclaration> functionDeclarations;

    public ToolDTO() {}

    public ToolDTO(List<FunctionDeclaration> functionDeclarations) {
        this.functionDeclarations = functionDeclarations;
    }

}

