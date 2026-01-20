package app.api;

import app.conversation.ConversationOrchestrator;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/chat")
public class ChatController {

    private final ConversationOrchestrator conversationOrchestrator;

    public ChatController(ConversationOrchestrator conversationOrchestrator) {
        this.conversationOrchestrator = conversationOrchestrator;
    }

    @PostMapping
    public ChatResponse chat(@RequestBody ChatRequest request) {

        UUID conversationId = request.conversationId() != null
                ? UUID.fromString(request.conversationId())
                : UUID.randomUUID();

        String reply = conversationOrchestrator.handleUserMessage(
                conversationId,
                request.message()
        );

        return new ChatResponse(conversationId.toString(), reply);
    }
}

