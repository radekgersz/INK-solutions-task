package app.agents;

import app.clients.LlmClient;
import app.conversation.ChatMessage;
import app.conversation.Conversation;
import app.conversation.Role;
import app.properties.PromptProperties;
import app.technical.Document;
import app.technical.DocumentSelector;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

import static app.agents.AgentType.TECHNICAL;

@Component
public class TechnicalAgent implements Agent {
    private final LlmClient llmClient;
    private final DocumentSelector documentSelector;
    private final String technicalPrompt;
    private final String noDocsPrompt;

    public TechnicalAgent(LlmClient llmClient,
                          DocumentSelector documentSelector,
                          PromptProperties promptProperties) {
        this.llmClient = llmClient;
        this.documentSelector = documentSelector;
        this.technicalPrompt = promptProperties.getTechnical();
        this.noDocsPrompt = promptProperties.getNoDocs();
    }
    @Override
    public String respond(Conversation conversation) {
        String userText = conversation.getLastUserMessage()
                .orElseThrow()
                .content();
        int NUM_RELEVANT_DOCUMENTS = 3;
        List<Document> documents = documentSelector.selectRelevant(userText, NUM_RELEVANT_DOCUMENTS);
        if (documents.isEmpty()) {
            return llmClient.generateResponse(List.of(
                    new ChatMessage(Role.SYSTEM, noDocsPrompt),
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
                new ChatMessage(Role.SYSTEM, technicalPrompt),
                new ChatMessage(Role.SYSTEM, context.toString()),
                new ChatMessage(Role.USER, userText)
        );
    }


}
