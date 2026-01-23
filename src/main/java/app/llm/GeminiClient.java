package app.llm;

import app.conversation.ChatMessage;
import app.conversation.Conversation;
import app.llm.dtos.requests.gemini.*;

import app.llm.dtos.responses.gemini.ContentResponseDTO;
import app.llm.dtos.responses.gemini.FunctionCallResponseDTO;
import app.llm.dtos.responses.gemini.PartResponseDTO;
import app.llm.dtos.responses.gemini.ResponseDTO;
import app.tools.ToolCall;
import app.tools.ToolSchema;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
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



    public LlmResponse generateResponse(Conversation conversation, List<ToolSchema> schema) {
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
            log.info(response.body());
            log.info(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(dto));
            log.info(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(parseResponse(dto)));
            return parseResponse(dto);

        } catch (Exception e) {
            throw new RuntimeException("Gemini API call failed", e);
        }

    }

    private LlmResponse parseResponse(ResponseDTO dto) {
        if (dto == null || dto.getCandidates() == null || dto.getCandidates().isEmpty()) {
            return new LlmResponse("", List.of());
        }

        ContentResponseDTO content = dto.getCandidates().getFirst().getContent();

        if (content == null || content.getParts() == null) {
            return new LlmResponse("", List.of());
        }

        StringBuilder textBuilder = new StringBuilder();
        List<ToolCall> toolCalls = new ArrayList<>();

        for (PartResponseDTO part : content.getParts()) {
            if (part == null) {
                continue;
            }

            // Collect text
            if (part.getText() != null) {
                textBuilder.append(part.getText());
            }

            // Collect function calls
            if (part.getFunctionCall() != null) {
                toolCalls.add(mapToToolCall(part.getFunctionCall()));
            }
        }

        return new LlmResponse(
                textBuilder.toString(),
                List.copyOf(toolCalls)
        );
    }
    private ToolCall mapToToolCall(FunctionCallResponseDTO dto) {
        return new ToolCall(
                dto.getName(),
                dto.getArgs()
        );
    }


    private RequestDTO createRequest() {

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

        ToolDTO tool = new ToolDTO(
                List.of(functionDeclaration)
        );

        return new RequestDTO(
                List.of(content),
                List.of(tool)
        );
    }
}

