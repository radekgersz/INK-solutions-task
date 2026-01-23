package app.llm.dtos.responses.gemini;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class FunctionCallResponseDTO {

    private String name;
    private Map<String, Object> args;

    public FunctionCallResponseDTO() {}
}

