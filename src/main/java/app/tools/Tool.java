package app.tools;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;
import java.util.function.Function;


public interface Tool {

    String name();
    String description();
    List<ToolArgument> arguments();
    String execute(Map<String, Object> arguments);
}
