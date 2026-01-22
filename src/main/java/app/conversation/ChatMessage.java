package app.conversation;

import lombok.Getter;


public record ChatMessage(Role role, String content) {
}
