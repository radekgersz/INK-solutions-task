package conversation;

import lombok.Getter;

@Getter
public class ChatMessage {
    private Role role;
    private String content;

    public ChatMessage(Role role, String content) {
        this.role = role;
        this.content = content;
    }
}
