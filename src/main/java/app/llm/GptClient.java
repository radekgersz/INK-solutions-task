package app.llm;

import app.conversation.ChatMessage;

import java.net.http.HttpClient;
import java.util.List;

public class GptClient implements LlmClient {

    private static final String API_URL = "https://api.openai.com/v1/chat/completions";
    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final String apiKey = System.getenv("OPENAI_API_KEY");


    @Override
    public String classifyMessage(List<ChatMessage> messages) {
        return "";
    }
}
