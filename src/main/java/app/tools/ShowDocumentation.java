package app.tools;

import java.util.List;
import java.util.Map;

public class ShowDocumentation implements Tool{

    @Override
    public String name() {
        return "";
    }

    @Override
    public String description() {
        return "";
    }

    @Override
    public List<ToolArgument> arguments() {
        return List.of();
    }

    @Override
    public String execute(Map<String, String> arguments) {
        return "";
    }
}
