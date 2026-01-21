package app.clients.dtos.responses.gemini;

import app.clients.dtos.requests.gemini.Content;

public record Candidate(
        Content content
) {}