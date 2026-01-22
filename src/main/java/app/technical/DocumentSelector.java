package app.technical;

import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import app.conversation.Conversation;
import app.conversation.ChatMessage;
import app.conversation.Role;

@Component
public class DocumentSelector {
    private static final int MIN_RELEVANCE_SCORE = 1;
    private final DocumentsCatalog catalog;

    public DocumentSelector(DocumentsCatalog catalog) {
        this.catalog = catalog;

    }

    public List<Document> selectRelevant(List<ChatMessage> messages, int limit) {
        String combinedUserText = messages.stream()
                .filter(m -> m.role() == Role.USER)
                .map(ChatMessage::content)
                .collect(Collectors.joining(" "));

        Set<String> tokens = tokenize(combinedUserText);
        List<ScoredDocument> scoredDocuments = catalog.getDocuments().stream()
                .map(doc -> new ScoredDocument(doc, score(doc, tokens)))
                .toList();

        List<ScoredDocument> relevantDocuments = scoredDocuments.stream()
                .filter(scored -> scored.score() >= MIN_RELEVANCE_SCORE)
                .toList();

        if (relevantDocuments.isEmpty()) {
            return List.of(); //no relevant docs
        }

        List<ScoredDocument> sortedDocuments = relevantDocuments.stream()
                .sorted(Comparator.comparingInt(ScoredDocument::score).reversed())
                .toList();

        return sortedDocuments.stream()
                .limit(limit)
                .map(ScoredDocument::document)
                .toList();


    }
    private int score(Document doc, Set<String> tokens) {
        int score = 0;
        for (String keyword : doc.getKeywords()) {
            if (tokens.contains(keyword.toLowerCase())) {
                score++;
            }
        }
        return score;
    }

    private Set<String> tokenize(String text) {
        return Arrays.stream(text.toLowerCase().split("\\W+")) .filter(s -> !s.isBlank()) .collect(Collectors.toSet());
    }


    private record ScoredDocument(Document document, int score){}
}
