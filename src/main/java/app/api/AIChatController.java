package app.api;

import app.llm.GeminiClient;
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
    private final GeminiClient geminiClientl;

    public AIChatController(ConversationOrchestrator conversationOrchestrator, GeminiClient geminiClientl) {
        this.geminiClientl = geminiClientl;
        this.conversationOrchestrator = conversationOrchestrator;
    }

    @PostMapping
    public String chat(@RequestBody UserRequest request) {

        UUID conversationId = request.conversationId() != null
                ? UUID.fromString(request.conversationId())
                : UUID.randomUUID();

        geminiClientl.generateResponse();
        return "";
//        String reply = conversationOrchestrator.handleMessage(
//                request.conversationHistory(),
//                request.userInput()
//        );

//        return new AIResponse(conversationId.toString(), reply);
    }
}

