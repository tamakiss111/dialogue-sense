package com.example.dialoguesense.assistant;

import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

/**
 * AI assistant that personalizes the application itself.
 */
public interface ApplicationAssistant {

    @UserMessage("{{message}}")
    String send(@V("message") String message);
}
