package app.llm;

import app.conversation.ChatMessage;
import app.llm.dtos.requests.gemini.Content;
import app.llm.dtos.requests.gemini.GeminiRequestDTO;
import app.llm.dtos.requests.gemini.GenerationConfig;
import app.llm.dtos.requests.gemini.Part;
import app.llm.dtos.responses.gemini.GeminiResponseDTO;
import app.tools.Tool;
import app.tools.ToolSchema;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import tools.jackson.databind.JsonNode;
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
    public LlmResponse generateResponse(List<ChatMessage> messages, List<ToolSchema> toolSchemas) {
        return null;
    }
}

