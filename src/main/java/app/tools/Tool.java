package app.tools;

import java.util.List;
import java.util.Map;


public interface Tool {

    String name();
    String description();
    List<ToolArgument> arguments();
    String execute(Map<String, String> arguments);
}
