package app.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "answers")
public class AgentAnswerProperties {
    private String subscribeMessage;
    private String cancelSubscriptionMessage;
}
