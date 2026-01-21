package app.llm;

import app.conversation.ChatMessage;

import java.net.http.HttpClient;
import java.util.List;
import java.util.stream.Collectors;

public class GeminiClient implements LlmClient {

    private static final String API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent";

    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final String apiKey = System.getenv("GEMINI_API_KEY");

    @Override
    public String classifyMessage(List<ChatMessage> messages) {
        String payload = messages
                .stream()
                .map(ChatMessage::getContent)
                .collect(Collectors.joining("\n\n"));

        return "";
    }
}
