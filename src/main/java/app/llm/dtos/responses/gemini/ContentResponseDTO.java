package app.llm.dtos.responses.gemini;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ContentResponseDTO {

    private List<PartResponseDTO> parts;
    private String role;

    public ContentResponseDTO() {
    }
}

