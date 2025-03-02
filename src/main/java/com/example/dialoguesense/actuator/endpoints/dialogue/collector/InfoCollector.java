package com.example.dialoguesense.actuator.endpoints.dialogue.collector;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.actuate.info.Info;
import org.springframework.boot.actuate.info.InfoContributor;

import java.util.Map;
import java.util.Optional;

/**
 * Collects general information about the application from all the enabled {@link InfoContributor}s.
 */
public class InfoCollector implements DialogueDataCollector {
    private static final Logger LOGGER = LoggerFactory.getLogger(InfoCollector.class);
    private final Map<String, InfoContributor> infoContributors;
    private final ObjectMapper objectMapper;

    public InfoCollector(Map<String, InfoContributor> infoContributors, ObjectMapper objectMapper) {
        this.infoContributors = infoContributors;
        this.objectMapper = objectMapper;
    }

    @Override
    public Optional<String> collect() {
        try {
            var infoBuilder = new Info.Builder();
            infoContributors.forEach((name, contributor) -> contributor.contribute(infoBuilder));

            var info = infoBuilder.build();
            var result = objectMapper.writeValueAsString(info);
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("The collected general information:{}", result);
            }

            return Optional.of(result);
        } catch (Exception e) {
            LOGGER.error("Collecting info from InfoContributors failed: ", e);
            return Optional.empty();
        }
    }


}
