package app.llm.dtos.requests.gemini;

import java.util.List;

public record Content(
        String role,
        List<Part> parts
) {}
