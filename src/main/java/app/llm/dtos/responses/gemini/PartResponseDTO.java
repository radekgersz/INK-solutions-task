package app.llm.dtos.responses.gemini;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PartResponseDTO {

    private FunctionCallResponseDTO functionCall;

    public PartResponseDTO() {}
}

