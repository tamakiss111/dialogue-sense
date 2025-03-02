package com.example.dialoguesense.actuator.endpoints.dialogue.collector.config;

import com.example.dialoguesense.actuator.endpoints.dialogue.collector.HealthCollector;
import com.example.dialoguesense.actuator.endpoints.dialogue.collector.InfoCollector;
import com.example.dialoguesense.actuator.endpoints.dialogue.collector.MetricsCollector;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.boot.actuate.metrics.MetricsEndpoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class DialogueDataCollectorConfig {

    @Bean
    HealthCollector healthCollector(Map<String, HealthIndicator> healthIndicators, ObjectMapper objectMapper) {
        return new HealthCollector(healthIndicators, objectMapper);
    }

    @Bean
    InfoCollector infoCollector(Map<String, InfoContributor> infoContributors, ObjectMapper objectMapper) {
        return new InfoCollector(infoContributors, objectMapper);
    }

    @Bean
    MetricsCollector metricsCollector(MetricsEndpoint metricsEndpoint, ObjectMapper objectMapper) {
        return new MetricsCollector(metricsEndpoint, objectMapper);
    }

    /**
     * Dummy health indicator of a user service which is always healthy.
     *
     * @return
     */
    @Bean
    HealthIndicator userServiceHealthIndicator() {
        return new HealthIndicator() {
            @Override
            public Health getHealth(boolean includeDetails) {
                return HealthIndicator.super.getHealth(includeDetails);
            }

            @Override
            public Health health() {
                return Health
                        .up()
                        .build();
            }
        };
    }

    /**
     * Dummy health indicator of an order service which is out of service.
     *
     * @return
     */
    @Bean
    HealthIndicator orderServiceHealthIndicator() {
        return new HealthIndicator() {
            @Override
            public Health getHealth(boolean includeDetails) {
                return HealthIndicator.super.getHealth(includeDetails);
            }

            @Override
            public Health health() {
                return Health
                        .outOfService()
                        .build();
            }
        };
    }

}
