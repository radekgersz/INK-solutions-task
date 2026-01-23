package app.llm.dtos.requests.gemini;

import java.util.List;

public record GeminiRequestDTO(
        List<Content> contents,
        GenerationConfig generationConfig
) {}

