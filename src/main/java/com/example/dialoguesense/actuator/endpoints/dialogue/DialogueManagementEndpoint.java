package com.example.dialoguesense.actuator.endpoints.dialogue;

import com.example.dialoguesense.assistant.ApplicationAssistant;
import org.springframework.boot.actuate.endpoint.annotation.WriteOperation;
import org.springframework.boot.actuate.endpoint.web.annotation.WebEndpoint;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * Management endpoint to discuss with the {@link ApplicationAssistant}.
 */
@Component
@WebEndpoint(id = "dialogue")
public class DialogueManagementEndpoint {
    private final ApplicationAssistant assistant;

    public DialogueManagementEndpoint(ApplicationAssistant assistant) {
        this.assistant = assistant;
    }

    @WriteOperation
    ResponseEntity<String> sendMessage(String message) {
        return ResponseEntity.ok(assistant.send(message));
    }
}
