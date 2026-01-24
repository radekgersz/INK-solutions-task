package app.tools;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Component
public class ListAvailablePlans implements Tool {
    @Override
    public String name() {
        return "listAvailablePlans";
    }

    @Override
    public String description() {
        return "list all available subscription plans to the user";
    }

    @Override
    public List<ToolArgument> arguments() {
        return List.of();
    }

    @Override
    public String execute(Map<String, Object> arguments) {
        return "";
    }
}
