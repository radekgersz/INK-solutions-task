package app.api;

import app.conversation.Conversation;
import app.conversation.ConversationMemory;
import app.orchestrator.ConversationOrchestrator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/chat")
@Slf4j
public class AIChatController {

    private final ConversationOrchestrator conversationOrchestrator;
    private final ConversationMemory memory;

    public AIChatController(ConversationOrchestrator conversationOrchestrator, ConversationMemory memory) {
        this.memory = memory;
        this.conversationOrchestrator = conversationOrchestrator;
    }

    @PostMapping
    public String chat(@RequestBody UserRequest request) {

        UUID conversationId = request.conversationId() != null
                ? UUID.fromString(request.conversationId())
                : UUID.randomUUID();

        Conversation conversation = memory.getOrCreateConversation(conversationId);
        conversation.addUserMessage(request.message());

        String reply = conversationOrchestrator.handleMessage(
                conversation,
                request.message()
        );
        log.info(reply);
        return "";
//        return new AIResponse(conversationId.toString(), reply);
    }
}

