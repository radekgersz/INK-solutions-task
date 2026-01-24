package app.llm;

import app.conversation.ChatMessage;
import app.conversation.Conversation;
import app.llm.dtos.requests.gemini.*;

import app.llm.dtos.responses.gemini.ResponseDTO;
import app.tools.Tool;
import app.tools.ToolRegistry;
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

import static app.llm.GeminiParser.createRequest;
import static app.llm.GeminiParser.parseResponse;

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
    private static final int NUM_MESSAGES = 5;


    public LlmResponse generateResponse(Conversation conversation, ToolRegistry toolRegistry) {
        String API_URL = BASE_URL + GEMINI_MODEL + ":generateContent";
        try {

            List<ChatMessage> lastMessages = conversation.getLastNMessages(NUM_MESSAGES);
            Map<String,Tool> tools = toolRegistry.getTools();
            RequestDTO request = createRequest(lastMessages,tools);
//            RequestDTO request = createDummyRequest();
            log.info(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(request));
            String json = mapper.writeValueAsString(request);
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create(API_URL + "?key=" + apiKey))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            ObjectMapper mapper = new ObjectMapper();

            ResponseDTO dto = mapper.readValue(response.body(), ResponseDTO.class);
            log.info(response.body());
            log.info(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(dto));
            log.info(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(parseResponse(dto)));
            return parseResponse(dto);

        } catch (Exception e) {
            throw new RuntimeException("Gemini API call failed", e);
        }

    }


    private RequestDTO createDummyRequest() {

        // --- parts ---
        Part part = new Part("What is the weather like in Warsaw and Tokyo and Delhi?");
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

        ToolRequestDTO tool = new ToolRequestDTO(
                List.of(functionDeclaration)
        );

        return new RequestDTO(
                List.of(content),
                List.of(tool)
        );
    }
}

