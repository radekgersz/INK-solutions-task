package app.agents;

import app.clients.LlmClient;
import app.conversation.ChatMessage;
import app.conversation.Conversation;
import app.conversation.Role;
import app.technical.Document;
import app.technical.DocumentSelector;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

import static app.agents.AgentType.TECHNICAL;

@Component
@AllArgsConstructor
public class TechnicalAgent implements Agent {
    private LlmClient llmClient;
    private DocumentSelector documentSelector;
    private static final String TECHNICAL_PROMPT = """
            You are a technical support agent.
            Answer the user's question using ONLY the provided documentation.
            If the documentation does not contain the answer, say:
            "The provided documentation does not cover this question."
            Do not use outside knowledge.
            """;
    private static final String NO_DOCS_PROMPT = """
            You are a technical support assistant.
            The user has asked a question, but there is no relevant documentation available.
            If the user is asking generally whether you can help, what you can do, or how to ask a question, respond positively and ask them to provide more details.
            If the user asked a specific technical question but there is no documentation, Clearly say you don't have documentation to answer it reliably.
            Ask targeted clarifying questions collect the minimum information needed. Do NOT make up answers. Do NOT give concrete product instructions
            """;
    @Override
    public String respond(Conversation conversation) {
        String userText = conversation.getLastUserMessage()
                .orElseThrow()
                .content();
        int NUM_RELEVANT_DOCUMENTS = 3;
        List<Document> documents = documentSelector.selectRelevant(userText, NUM_RELEVANT_DOCUMENTS);
        if (documents.isEmpty()) {
            return llmClient.generateResponse(List.of(
                    new ChatMessage(Role.SYSTEM, NO_DOCS_PROMPT),
                    new ChatMessage(Role.USER, userText)
            ));
        }
        List<ChatMessage> prompt = buildPrompt(userText, documents);
        return llmClient.generateResponse(prompt);
    }

    @Override
    public AgentType type() {
        return TECHNICAL;
    }

    private List<ChatMessage> buildPrompt(String userText, List<Document> documents) {
        StringBuilder context = new StringBuilder();
        for (Document document : documents) {
            context.append("###")
                    .append(document.getTitle())
                    .append("\n")
                    .append(document.getContent())
                    .append("\n\n");
        }
        return List.of(
                new ChatMessage(Role.SYSTEM, TECHNICAL_PROMPT),
                new ChatMessage(Role.SYSTEM, context.toString()),
                new ChatMessage(Role.USER, userText)
        );
    }


}
