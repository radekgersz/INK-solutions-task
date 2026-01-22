package app.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "prompts")
public class PromptProperties {
    private String routing;
    private String technical;
    private String intent;
    private String outOfScope;
    private String noDocs;
    private String initialPrompt;
}
