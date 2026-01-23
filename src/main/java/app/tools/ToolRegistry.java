package app.tools;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class ToolRegistry {

    private final Map<String, Tool> tools = new HashMap<>();

    public ToolRegistry(List<Tool> toolList) {
        for (Tool tool : toolList) {
            tools.put(tool.getName(), tool);
        }
    }

    public List<ToolSchema> getToolSchemas() {
        return tools.values()
                .stream()
                .map(Tool::toSchema)
                .toList();
    }

    public String execute(ToolCall call) {
        Tool tool = tools.get(call.getName());
        if (tool == null) {
            throw new IllegalStateException(
                    "Unknown tool requested: " + call.getName()
            );
        }
        return tool.execute(call.getArguments());
    }
}
