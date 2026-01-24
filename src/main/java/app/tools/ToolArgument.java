package app.tools;

import lombok.Getter;

@Getter
public class ToolArgument {

    private final String name;
    private final String type;        // "string", "number", "boolean"
    private final boolean required;
    private final String description;

    public ToolArgument(
            String name,
            String type,
            boolean required,
            String description
    ) {
        this.name = name;
        this.type = type;
        this.required = required;
        this.description = description;
    }
}
