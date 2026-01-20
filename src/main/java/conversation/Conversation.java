package conversation;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

import static conversation.Role.ASSISTANT;
import static conversation.Role.USER;

@Getter
public class Conversation {
    private final int conversationID;


    private ArrayList<ChatMessage> messages = new ArrayList<>();

    public Conversation(int conversationID){
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
}
