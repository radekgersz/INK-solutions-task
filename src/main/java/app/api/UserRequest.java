package app.api;

public record UserRequest(
        String conversationId,
        String message
) {}

