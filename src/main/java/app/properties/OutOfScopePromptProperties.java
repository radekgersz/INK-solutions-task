package app.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "prompts.outofscope")
public class OutOfScopePromptProperties {
    private String outOfScope;
}
