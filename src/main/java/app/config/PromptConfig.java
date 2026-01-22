package app.config;

import app.properties.BillingPromptProperties;
import app.properties.OutOfScopePromptProperties;
import app.properties.RoutingPromptProperties;
import app.properties.TechnicalPromptProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({
        RoutingPromptProperties.class,
        BillingPromptProperties.class,
        TechnicalPromptProperties.class,
        OutOfScopePromptProperties.class
})
public class PromptConfig {}
