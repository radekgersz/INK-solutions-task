package app.api;

public record ChatResponse(
        String conversationId,
        String reply
) {}
