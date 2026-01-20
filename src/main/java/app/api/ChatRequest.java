package app.api;

public record ChatRequest(
        String conversationId,
        String message
) {}

