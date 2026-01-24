package app.conversation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static app.conversation.Role.ASSISTANT;
import static app.conversation.Role.USER;

@Getter
@AllArgsConstructor
public class Conversation {
    private final UUID conversationID;
    private final ArrayList<ChatMessage> messages = new ArrayList<>();


    public void addUserMessage(String content) {
        messages.add(new ChatMessage(USER, content));
    }

    public void addAssistantMessage(String content) {
        messages.add(new ChatMessage(ASSISTANT, content));
    }
    public List<ChatMessage> getLastNMessages(int n) {
        if (n <= 0 || messages.isEmpty()) {
            return List.of();
        }

        int size = messages.size();
        int fromIndex = Math.max(0, size - n);

        return List.copyOf(messages.subList(fromIndex, size));
    }
}
