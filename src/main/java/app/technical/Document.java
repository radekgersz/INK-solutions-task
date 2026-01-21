package app.technical;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class Document {
    String id;
    String title;
    String content;
    List<String> keywords;
}