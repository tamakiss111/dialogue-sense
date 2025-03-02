package com.example.dialoguesense.actuator.endpoints.dialogue.collector;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.actuate.metrics.MetricsEndpoint;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Collects metrics information from all the available {@link MetricsEndpoint.MetricDescriptor}s.
 */
public class MetricsCollector implements DialogueDataCollector {
    private static final Logger LOGGER = LoggerFactory.getLogger(MetricsCollector.class);
    private final MetricsEndpoint metricsEndpoint;
    private final ObjectMapper objectMapper;

    public MetricsCollector(MetricsEndpoint metricsEndpoint, ObjectMapper objectMapper) {
        this.metricsEndpoint = metricsEndpoint;
        this.objectMapper = objectMapper;
    }

    @Override
    public Optional<String> collect() {
        try {
            var metricNames = metricsEndpoint.listNames().getNames();
            List<MetricsEndpoint.MetricDescriptor> metricDescriptors = new ArrayList<>(metricNames.size());
            metricNames.forEach(name -> metricDescriptors.add(metricsEndpoint.metric(name, null)));

            var aggregatedMetricResponses = new AggregatedMetricResponse(metricDescriptors);
            var result = objectMapper.writeValueAsString(aggregatedMetricResponses);
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("The collected metrics information:{}", result);
            }

            return Optional.of(result);
        } catch (Exception e) {
            LOGGER.error("Collecting metrics failed: ", e);
            return Optional.empty();
        }
    }

    private record AggregatedMetricResponse(List<MetricsEndpoint.MetricDescriptor> metrics) {
    }
}
