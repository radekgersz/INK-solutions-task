package app.api;

import app.orchestrator.ConversationOrchestrator;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/chat")
public class AIChatController {

    private final ConversationOrchestrator conversationOrchestrator;

    public AIChatController(ConversationOrchestrator conversationOrchestrator) {
        this.conversationOrchestrator = conversationOrchestrator;
    }

    @PostMapping
    public AIResponse chat(@RequestBody UserRequest request) {

        UUID conversationId = request.conversationId() != null
                ? UUID.fromString(request.conversationId())
                : UUID.randomUUID();

        String reply = conversationOrchestrator.handleMessage(
                request.conversationHistory(),
                request.userInput()
        );

        return new AIResponse(conversationId.toString(), reply);
    }
}

