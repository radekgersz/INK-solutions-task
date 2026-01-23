package app.tools;
import java.util.Map;
import java.util.function.Function;

public class Tool {

    private final String name;
    private final String description;
    private final Function<Map<String, String>, String> executor;

    public Tool(
            String name,
            String description,
            Function<Map<String, String>, String> executor
    ) {
        this.name = name;
        this.description = description;
        this.executor = executor;
    }

    public String getName() {
        return name;
    }

    public ToolSchema toSchema() {
        return new ToolSchema(name, description);
    }

    public String execute(Map<String, String> arguments) {
        return executor.apply(arguments);
    }
}
