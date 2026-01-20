package app.conversation;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class ConversationMemory {
    private final Map<UUID, Conversation> conversations = new ConcurrentHashMap<>();

    Conversation getOrCreateConversation(UUID id){
        return conversations.computeIfAbsent(
                id,
                Conversation::new
        );
    }
}
