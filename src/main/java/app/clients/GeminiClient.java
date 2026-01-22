package app.clients;

import app.conversation.ChatMessage;
import app.clients.dtos.requests.gemini.Content;
import app.clients.dtos.requests.gemini.GeminiRequestDTO;
import app.clients.dtos.requests.gemini.GenerationConfig;
import app.clients.dtos.requests.gemini.Part;
import app.clients.dtos.responses.gemini.GeminiResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class GeminiClient implements LlmClient {

    @Value("${gemini.model}")
    private String GEMINI_MODEL;
    @Value("${base.url}")
    private String BASE_URL;

    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final String apiKey = System.getenv("GEMINI_API_KEY");
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public String generateResponse(List<ChatMessage> messages) {
        String API_URL = BASE_URL + GEMINI_MODEL + ":generateContent";

        try {
            GeminiRequestDTO request = createRequest(messages);
            String json = mapper.writeValueAsString(request);

            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create(API_URL + "?key=" + apiKey))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

            GeminiResponseDTO geminiResponse = mapper.readValue(response.body(), GeminiResponseDTO.class);
            return extractText(geminiResponse);

        } catch (Exception e) {
            throw new RuntimeException("Gemini API call failed", e);
        }
    }
    private GeminiRequestDTO createRequest(List<ChatMessage> messages){
        String payload = messages
                .stream()
                .map(ChatMessage::content)
                .collect(Collectors.joining("\n\n"));

        return new GeminiRequestDTO(
                List.of(
                        new Content(
                                "user",
                                List.of(new Part(payload))
                        )
                ),
                new GenerationConfig(0.0)
        );
    }
    private String extractText(GeminiResponseDTO response) {
        return response.candidates()
                .getFirst()
                .content()
                .parts()
                .getFirst()
                .text();
    }
}

