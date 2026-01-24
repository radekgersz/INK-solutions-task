package app.tools;

import app.rag.DocumentSelector;
import app.rag.Document;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ShowDocumentation implements Tool{

    private final DocumentSelector selector;

    public ShowDocumentation(DocumentSelector selector) {
        this.selector = selector;
    }

    @Override
    public String name() {
        return "retrieve_docs"; // matches the system prompt expectation
    }

    @Override
    public String description() {
        return "Retrieve relevant documentation snippets based on a query";
    }

    @Override
    public List<ToolArgument> arguments() {
        return List.of(new ToolArgument("query", "string", true, "Search query or keywords"));
    }

    @Override
    public String execute(Map<String, String> arguments) {
        String query = arguments.getOrDefault("query", "");
        if (query.isBlank()) return "";
        List<Document> docs = selector.selectByQuery(query, 3);
        if (docs.isEmpty()) return "No relevant documentation found.";
        return docs.stream()
                .map(d -> "Title: " + d.title() + "\n" + d.content())
                .collect(Collectors.joining("\n\n---\n\n"));
    }
}
