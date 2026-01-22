package app.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "prompts.billing")
public class BillingPromptProperties {
    private String intentPrompt;
    private String billingOutOfScopePrompt;
}
