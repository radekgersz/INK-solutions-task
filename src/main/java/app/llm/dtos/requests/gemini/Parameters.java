package app.llm.dtos.requests.gemini;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
public class Parameters {

    private String type;
    private Map<String, Property> properties;
    private List<String> required;

    public Parameters() {}

    public Parameters(String type, Map<String, Property> properties, List<String> required) {
        this.type = type;
        this.properties = properties;
        this.required = required;
    }

}

