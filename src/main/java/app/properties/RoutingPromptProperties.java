package app.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "prompts.routing")
public class RoutingPromptProperties {
    private String routing;
    private String initialPrompt;

}
