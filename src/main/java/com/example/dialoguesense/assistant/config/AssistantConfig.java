package com.example.dialoguesense.assistant.config;

import com.example.dialoguesense.actuator.endpoints.dialogue.collector.HealthCollector;
import com.example.dialoguesense.actuator.endpoints.dialogue.collector.InfoCollector;
import com.example.dialoguesense.actuator.endpoints.dialogue.collector.MetricsCollector;
import com.example.dialoguesense.assistant.ApplicationAssistant;
import com.example.dialoguesense.configprops.AiConfigurationProperties;
import dev.langchain4j.agent.tool.ToolSpecification;
import dev.langchain4j.memory.chat.TokenWindowChatMemory;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.openai.OpenAiChatModelName;
import dev.langchain4j.model.openai.OpenAiTokenizer;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.tool.ToolExecutor;
import dev.langchain4j.store.memory.chat.InMemoryChatMemoryStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class AssistantConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger(AssistantConfig.class);

    @Bean
    ChatLanguageModel chatLanguageModel(AiConfigurationProperties aiConfigurationProperties) {
        return OpenAiChatModel.builder()
                .apiKey(aiConfigurationProperties.apiKey())
                .modelName(OpenAiChatModelName.GPT_4_O)
                .temperature(0.5)
                .logRequests(true)
                .logResponses(true)
                .build();
    }

    @Bean
    ApplicationAssistant applicationAssistant(ChatLanguageModel chatLanguageModel,
                                              @Qualifier("healthCollectorToolExecutor") ToolExecutor healthCollectorToolExecutor,
                                              @Qualifier("infoCollectorToolExecutor") ToolExecutor infoCollectorToolExecutor,
                                              @Qualifier("metricsCollectorToolExecutor") ToolExecutor metricsCollectorToolExecutor) {
        return AiServices
                .builder(ApplicationAssistant.class)
                .chatLanguageModel(chatLanguageModel)
                .systemMessageProvider(memoryId -> """
                        You are an application called dialogue-sense.
                        Answer in a human way how you feel mentally or physically.
                        You can get information about yourself through the provided tools.
                        Although these are technical information, try transform them to your current feelings.
                        """)
                .tools(Map.of(
                        ToolSpecification.builder()
                                .name("healthCollectorTool")
                                .description("This tool provides information about your health or the health of your most important components.")
                                .build(), healthCollectorToolExecutor,
                        ToolSpecification.builder()
                                .name("infoCollectorTool")
                                .description("This tool provides general information about you.")
                                .build(), infoCollectorToolExecutor,
                        ToolSpecification.builder()
                                .name("metricsCollectorTool")
                                .description("This tool provides information about the metrics of you.")
                                .build(), metricsCollectorToolExecutor
                ))
                .chatMemory(TokenWindowChatMemory.builder()
                        .chatMemoryStore(new InMemoryChatMemoryStore())
                        .maxTokens(20000, new OpenAiTokenizer(OpenAiChatModelName.GPT_4_O))
                        .build())
                .build();
    }

    @Bean("healthCollectorToolExecutor")
    public ToolExecutor healthCollectorToolExecutor(HealthCollector healthCollector) {
        return (toolExecutionRequest, memoryId) -> {
            if(LOGGER.isDebugEnabled()) {
                LOGGER.debug("Executing health tool with request: {}", toolExecutionRequest);
            }
            return healthCollector.collect()
                    .orElse("There is no available health information!");
        };
    }

    @Bean("infoCollectorToolExecutor")
    public ToolExecutor infoCollectorToolExecutor(InfoCollector infoCollector) {
        return (toolExecutionRequest, memoryId) -> {
            if(LOGGER.isDebugEnabled()){
                LOGGER.debug("Executing info tool with request: {}", toolExecutionRequest);
            }
            return infoCollector.collect()
                    .orElse("There is no available general information!");
        };
    }

    @Bean("metricsCollectorToolExecutor")
    public ToolExecutor metricsCollectorToolExecutor(MetricsCollector metricsCollector) {
        return (toolExecutionRequest, memoryId) -> {
            if(LOGGER.isDebugEnabled()){
                LOGGER.debug("Executing metrics tool with request: {}", toolExecutionRequest);
            }
            return metricsCollector.collect()
                    .orElse("There is no available metrics information!");
        };
    }
}
