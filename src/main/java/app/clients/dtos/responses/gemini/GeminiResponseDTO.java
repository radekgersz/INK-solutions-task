package app.clients.dtos.responses.gemini;

import java.util.List;

public record GeminiResponseDTO(
        List<Candidate> candidates
) {}
