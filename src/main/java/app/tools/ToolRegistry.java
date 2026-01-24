package app.tools;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@Getter
public class ToolRegistry {

    private final Map<String, Tool> tools = new HashMap<>();

    public ToolRegistry(List<Tool> toolList) {
        for (Tool tool : toolList) {
            tools.put(tool.name(), tool);
        }
    }

    public String execute(ToolCall call) {
        Tool tool = tools.get(call.name());
        if (tool == null) {
            throw new IllegalStateException(
                    "Unknown tool requested: " + call.name()
            );
        }
        return tool.execute(call.arguments());
    }
}
