package app.llm.dtos.requests.gemini;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ToolRequestDTO {

    private List<FunctionDeclaration> functionDeclarations;

    public ToolRequestDTO() {}

    public ToolRequestDTO(List<FunctionDeclaration> functionDeclarations) {
        this.functionDeclarations = functionDeclarations;
    }

}

