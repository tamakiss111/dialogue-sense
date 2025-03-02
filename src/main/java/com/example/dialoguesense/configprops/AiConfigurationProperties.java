package com.example.dialoguesense.configprops;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("dialogue-sense.ai")
public record AiConfigurationProperties(String apiKey) {
}
