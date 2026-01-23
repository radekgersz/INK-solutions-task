package app.llm;

import app.conversation.ChatMessage;
import app.llm.dtos.requests.gemini.*;

import app.llm.dtos.responses.gemini.ResponseDTO;
import app.tools.ToolSchema;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;

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



    public String generateResponse() {
        String API_URL = BASE_URL + GEMINI_MODEL + ":generateContent";
        try {
            RequestDTO request = createRequest();
            String json = mapper.writeValueAsString(request);
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create(API_URL + "?key=" + apiKey))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            ObjectMapper mapper = new ObjectMapper();

            ResponseDTO dto = mapper.readValue(response.body(), ResponseDTO.class);
            return "";

        } catch (Exception e) {
            throw new RuntimeException("Gemini API call failed", e);
        }

    }

    private RequestDTO createRequest() {

        // --- parts ---
        Part part = new Part("What is the weather like on Mars?");
        Content content = new Content(
                "user",
                List.of(part)
        );

        // --- function parameters schema ---
        Property locationProperty = new Property(
                "string",
                "Name of a planet or city"
        );

        Parameters parameters = new Parameters(
                "object",
                Map.of("location", locationProperty),
                List.of("location")
        );

        // --- function declaration ---
        FunctionDeclaration functionDeclaration = new FunctionDeclaration(
                "get_current_temperature",
                "Returns the current temperature for a given location",
                parameters
        );

        ToolDTO tool = new ToolDTO(
                List.of(functionDeclaration)
        );

        return new RequestDTO(
                List.of(content),
                List.of(tool)
        );
    }

    @Override
    public String generateResponse(List<ChatMessage> messages, List<ToolSchema> toolSchemas) {
        return "";
    }
}

