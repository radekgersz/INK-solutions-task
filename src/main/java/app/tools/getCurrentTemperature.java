package app.tools;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class getCurrentTemperature implements Tool{
    @Override
    public String name() {
        return "get_current_temperature";
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
    public String execute(Map<String, Object> arguments) {
        return "eeeeeeeeee makarena";
    }
}
