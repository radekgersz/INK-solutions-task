package app.agents;

import app.clients.LlmClient;
import app.conversation.Conversation;
import app.data.BillingCatalog;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class BillingAgent implements Agent {

    private final BillingCatalog catalog;
    private final RefundCaseService refundService;
    private final LlmClient llmClient;
    @Override
    public String respond(Conversation conversation) {
        return "";
    }
}
