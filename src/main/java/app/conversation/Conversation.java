package app.conversation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import static app.conversation.Role.ASSISTANT;
import static app.conversation.Role.USER;

@Getter
@AllArgsConstructor
public class Conversation {
//    private final UUID conversationID;
//
//    private final ArrayList<ChatMessage> messages = new ArrayList<>();
//
//
//    public void addUserMessage(String content) {
//        messages.add(new ChatMessage(USER, content));
//    }
//
//    public void addAssistantMessage(String content) {
//        messages.add(new ChatMessage(ASSISTANT, content));
//    }
//    public Optional<ChatMessage> getLastUserMessage() {
//        for (int i = messages.size() - 1; i >= 0; i--) {
//            ChatMessage message = messages.get(i);
//            if (message.role() == Role.USER) {
//                return Optional.of(message);
//            }
//        }
//        return Optional.empty();
//    }
//    public int getUserMessageCount() {
//        int count = 0;
//        for (ChatMessage message : messages) {
//            if (message.role() == Role.USER) {
//                count++;
//            }
//        }
//        return count;
//    }
//    public List<ChatMessage> getLastNMessages(int n) {
//        int start = Math.max(0, messages.size() - n);
//        return messages.subList(start, messages.size());
//    }
}
