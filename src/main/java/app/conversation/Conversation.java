package app.conversation;

import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static app.conversation.Role.ASSISTANT;
import static app.conversation.Role.USER;

@Getter
public class Conversation {
    private final UUID conversationID;

    private final ArrayList<ChatMessage> messages = new ArrayList<>();

    public Conversation(UUID conversationID){
        this.conversationID = conversationID;
    }


    public List<ChatMessage> getMessages() {
        return List.copyOf(messages);
    }

    public void addUserMessage(String content) {
        messages.add(new ChatMessage(USER, content));
    }

    public void addAssistantMessage(String content) {
        messages.add(new ChatMessage(ASSISTANT, content));
    }
    public Optional<ChatMessage> getLastUserMessage() {
        for (int i = messages.size() - 1; i >= 0; i--) {
            ChatMessage message = messages.get(i);
            if (message.getRole() == Role.USER) {
                return Optional.of(message);
            }
        }
        return Optional.empty();
    }

}
