package app.conversation;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ConversationMemory {
    private final Map<UUID, Conversation> conversations = new ConcurrentHashMap<>();

    Conversation getOrCreateConversation(UUID id){
        return conversations.computeIfAbsent(
                id,
                Conversation::new
        );
    }
}
