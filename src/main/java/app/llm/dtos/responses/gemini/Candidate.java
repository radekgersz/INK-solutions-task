package app.llm.dtos.responses.gemini;

import app.llm.dtos.requests.gemini.Content;

public record Candidate(
        Content content
) {}