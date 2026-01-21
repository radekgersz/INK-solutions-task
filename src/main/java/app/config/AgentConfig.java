package app.config;

import app.agents.Agent;
import app.agents.AgentType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
public class AgentConfig {

    @Bean
    public Map<AgentType, Agent> agents(List<Agent> agents) {
        return agents.stream()
                .collect(Collectors.toUnmodifiableMap(
                        Agent::type,
                        Agent -> Agent
                ));
    }
}
