package app.llm.dtos;

import java.util.List;

record GeminiRequestDTO(
        List<Content> contents,
        GenerationConfig generationConfig
) {}

record Content(
        String role,
        List<Part> parts
) {}

record Part(
        String text
) {}

record GenerationConfig(
        double temperature
) {}

