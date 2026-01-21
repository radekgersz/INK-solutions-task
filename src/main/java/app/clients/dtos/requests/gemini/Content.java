package app.clients.dtos.requests.gemini;

import java.util.List;

public record Content(
        String role,
        List<Part> parts
) {}
