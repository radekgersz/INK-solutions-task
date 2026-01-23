package app.api;

import java.util.UUID;

public record UserRequest(
        String conversationId,
        String message
) {}

