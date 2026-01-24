package app.tools;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class Subscribe implements Tool{
    @Override
    public String name() {
        return "subscribe";
    }

    @Override
    public String description() {
        return "Sends a user a form to subscribe to a plan";
    }

    @Override
    public List<ToolArgument> arguments() {
        return List.of();
    }

    @Override
    public String execute(Map<String, String> arguments) {
        return "    To start a subscription, please provide the following details:\n" +
                "\n" +
                "    - Plan name (Free, Pro, Custom, Enterprise)\n" +
                "    - Billing email address\n" +
                "\n" +
                "    Please reply in the following format:\n" +
                "    Plan: <plan name>\n" +
                "    Email: <your email>";
    }
}
