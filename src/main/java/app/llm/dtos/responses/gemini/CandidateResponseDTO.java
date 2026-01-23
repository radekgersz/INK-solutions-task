package app.llm.dtos.responses.gemini;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CandidateResponseDTO {

    private ContentResponseDTO content;

    public CandidateResponseDTO() {}
}

