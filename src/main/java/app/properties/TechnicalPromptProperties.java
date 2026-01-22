package app.properties;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "prompts.technical")
public class TechnicalPromptProperties {
    private String technical;
    private String noDocs;

}
