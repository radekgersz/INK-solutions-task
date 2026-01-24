package app.tools;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class CancelSubscription implements Tool{
    @Override
    public String name() {
        return "cancel_subscription";
    }

    @Override
    public String description() {
        return "Sends back a form to cancel the user's subscription";
    }

    @Override
    public List<ToolArgument> arguments() {
        return List.of();
    }

    @Override
    public String execute(Map<String, Object> arguments) {
        return "    To cancel a subscription, please provide the following details:\n" +
                "    \n" +
                "    - Client ID\n" +
                "    - Billing email address\n" +
                "    - Reason for cancellation (optional)\n" +
                "    \n" +
                "    Please reply in the following format:\n" +
                "    Client ID: <your client id>\n" +
                "    Email: <your email>\n" +
                "    Reason: <your reason>";
    }
}
