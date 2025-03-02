package com.example.dialoguesense.actuator.endpoints.dialogue.collector;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Collects health information from all the available health indicators.
 */
public class HealthCollector implements DialogueDataCollector {
    private static final Logger LOGGER = LoggerFactory.getLogger(HealthCollector.class);
    private final Map<String, HealthIndicator> healthIndicators;
    private final ObjectMapper objectMapper;

    public HealthCollector(Map<String, HealthIndicator> healthIndicators, ObjectMapper objectMapper) {
        this.healthIndicators = healthIndicators;
        this.objectMapper = objectMapper;
    }

    @Override
    public Optional<String> collect() {
        try {
            var aggregatedHealthResponses = new AggregatedHealthResponse(healthIndicators.entrySet()
                    .stream()
                    .collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().getHealth(true))));
            var result = objectMapper.writeValueAsString(aggregatedHealthResponses);
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("The collected health information:{}", result);
            }
            return Optional.of(result);
        } catch (Exception e) {
            LOGGER.error("Collecting health information failed: ", e);
            return Optional.empty();
        }
    }

    private record AggregatedHealthResponse(Map<String, Health> healths) {
    }

}
