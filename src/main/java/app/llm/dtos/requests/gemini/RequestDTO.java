package app.llm.dtos.requests.gemini;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RequestDTO {

    private List<Content> contents;
    private List<ToolDTO> tools;

    public RequestDTO() {}

    public RequestDTO(List<Content> contents, List<ToolDTO> tools) {
        this.contents = contents;
        this.tools = tools;
    }
}
