package app.tools;

public class ToolSchema {

    private final String name;
    private final String description;

    public ToolSchema(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}

