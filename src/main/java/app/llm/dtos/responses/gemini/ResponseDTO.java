package app.llm.dtos.responses.gemini;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ResponseDTO {

    private List<CandidateResponseDTO> candidates;

    public ResponseDTO() {}
}

