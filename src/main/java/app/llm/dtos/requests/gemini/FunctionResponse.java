package app.llm.dtos.requests.gemini;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class FunctionResponse {

    private String name;
    private Map<String, String> response;
}
