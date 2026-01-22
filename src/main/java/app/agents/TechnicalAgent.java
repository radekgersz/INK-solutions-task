package app.agents;

import app.clients.LlmClient;
import app.conversation.ChatMessage;
import app.conversation.Conversation;
import app.conversation.Role;
import app.properties.TechnicalPromptProperties;
import app.technical.Document;
import app.technical.DocumentSelector;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

import static app.agents.AgentType.TECHNICAL;
import static app.conversation.Role.SYSTEM;
import static app.conversation.Role.USER;

@Component
public class TechnicalAgent implements Agent {
    private final LlmClient llmClient;
    private final DocumentSelector documentSelector;
    private final String technicalPrompt;
    private final String noDocsPrompt;
    private static final int NUM_MESSAGES = 3;
    private static final int NUM_RELEVANT_DOCUMENTS = 3;

    public TechnicalAgent(LlmClient llmClient,
                          DocumentSelector documentSelector,
                          TechnicalPromptProperties technicalPromptProperties) {
        this.llmClient = llmClient;
        this.documentSelector = documentSelector;
        this.technicalPrompt = technicalPromptProperties.getTechnicalPrompt();
        this.noDocsPrompt = technicalPromptProperties.getNoDocsPrompt();
    }
    @Override
    public String respond(Conversation conversation) {
        List<ChatMessage> chatMessages = conversation.getLastNMessages(NUM_MESSAGES);
        List<Document> documents = documentSelector.selectRelevant(chatMessages, NUM_RELEVANT_DOCUMENTS);
        if (documents.isEmpty()) {
            LinkedList<ChatMessage> prompt = new LinkedList<>(chatMessages);
            prompt.addFirst(new ChatMessage(Role.SYSTEM, noDocsPrompt));
            return llmClient.generateResponse(prompt);
        }
        List<ChatMessage> prompt = buildPrompt(chatMessages, documents);
        return llmClient.generateResponse(prompt);
    }

    @Override
    public AgentType type() {
        return TECHNICAL;
    }

    private List<ChatMessage> buildPrompt(List<ChatMessage> chatMessages, List<Document> documents) {
        StringBuilder context = new StringBuilder();
        for (Document document : documents) {
            context.append("###")
                    .append(document.getTitle())
                    .append("\n")
                    .append(document.getContent())
                    .append("\n\n");
        }
        LinkedList<ChatMessage> prompt = new LinkedList<>(chatMessages);
        prompt.addFirst(new ChatMessage(Role.SYSTEM, context.toString()));
        prompt.addFirst(new ChatMessage(Role.SYSTEM, technicalPrompt));
        return prompt;
    }
}
