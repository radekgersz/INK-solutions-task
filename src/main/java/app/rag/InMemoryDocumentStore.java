package app.rag;

import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class InMemoryDocumentStore {

    private final List<Document> documents = new ArrayList<>();

    public InMemoryDocumentStore() {
        // empty constructor - documents can be added programmatically or via other init code
    }

    public void add(Document doc) {
        documents.add(doc);
    }

    public List<Document> all() {
        return Collections.unmodifiableList(documents);
    }

    public Optional<Document> findById(String id) {
        return documents.stream().filter(d -> d.id().equals(id)).findFirst();
    }

    public List<Document> searchByKeywords(List<String> keywords, int limit) {
        if (keywords == null || keywords.isEmpty()) {
            return Collections.emptyList();
        }
        return documents.stream()
                .map(d -> new ScoredDocument(d, score(d, keywords)))
                .filter(sd -> sd.score() > 0)
                .sorted((a, b) -> Double.compare(b.score(), a.score()))
                .limit(limit)
                .map(ScoredDocument::document)
                .collect(Collectors.toList());
    }

    private double score(Document d, List<String> keywords) {
        String text = (d.title() + " \n " + d.content()).toLowerCase();
        double matches = 0.0;
        for (String k : keywords) {
            if (k == null || k.isBlank()) continue;
            if (text.contains(k.toLowerCase())) matches += 1.0;
        }
        return matches / Math.max(1, keywords.size());
    }

    private record ScoredDocument(Document document, double score){}
}
