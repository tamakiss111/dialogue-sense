package com.example.dialoguesense.actuator.endpoints.dialogue.collector;

import com.example.dialoguesense.assistant.ApplicationAssistant;

import java.util.Optional;

/**
 * {@link DialogueDataCollector}s are used to feed the {@link ApplicationAssistant} with information about the application.
 */
@FunctionalInterface
public interface DialogueDataCollector {

    /**
     * Provides information about the application as a string.
     *
     * @return the collected information or an empty optional.
     */
    Optional<String> collect();
}
