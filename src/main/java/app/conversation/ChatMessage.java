package app.conversation;

public record ChatMessage(
        Role role,
        String content
) {}
