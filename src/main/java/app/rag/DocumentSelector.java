package app.rag;

import app.conversation.Conversation;
import app.conversation.ChatMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Component
public class DocumentSelector {

    private final InMemoryDocumentStore store;
    private final int numMessages = 3;

    @Autowired
    public DocumentSelector(InMemoryDocumentStore store) {
        this.store = store;
    }

    public List<Document> select(Conversation conversation, int k) {
        List<ChatMessage> recent = conversation.getLastNMessages(numMessages);

        // Gather all user messages (not only last) into a single keyword list
        List<String> keywords = new ArrayList<>();
        for (ChatMessage m : recent) {
            if (m.role().name().equalsIgnoreCase("USER")) {
                keywords.addAll(extractKeywords(m.content()));
            }
        }

        if (keywords.isEmpty()) return List.of();

        List<Document> found = store.searchByKeywords(keywords, k);

        // As an extra step, sort documents by the number of matched keywords (descending)
        return found.stream()
                .sorted(Comparator.comparingDouble(d -> -scoreDocument(d, keywords)))
                .collect(Collectors.toList());
    }

    // Keep selectByQuery helper
    public List<Document> selectByQuery(String query, int k) {
        List<String> keywords = extractKeywords(query);
        if (keywords.isEmpty()) return List.of();
        return store.searchByKeywords(keywords, k);
    }

    private double scoreDocument(Document d, List<String> keywords) {
        String text = (d.title() + " \n " + d.content()).toLowerCase(Locale.ROOT);
        double matches = 0.0;
        for (String k : keywords) {
            if (k == null || k.isBlank()) continue;
            if (text.contains(k.toLowerCase(Locale.ROOT))) matches += 1.0;
        }
        return matches;
    }

    private List<String> extractKeywords(String text) {
        if (text == null || text.isBlank()) return List.of();
        // Very naive extraction: split on non-word chars and filter short tokens
        return Arrays.stream(text.split("\\W+"))
                .map(String::toLowerCase)
                .filter(s -> s.length() > 2)
                .distinct()
                .collect(Collectors.toList());
    }
}
