package com.example.rest.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.googleai.GoogleAiGeminiChatModel;
import dev.langchain4j.model.ollama.OllamaChatModel;


// Configuração do modelo de LLM Controla qual provedor de IA é utilizado (Ollama local ou Gemini)
// CUIDADO: As propriedades do Ollama foram movidas para {@code app.ollama.*}
// (e NÃO {@code langchain4j.ollama.*}) para evitar que o starter auto-configure
// um bean concorrente e cause o erro "multiple beans of type ChatLanguageModel". NÃO PODE TER mais de um @code ChatLanguageModel
@Configuration
public class LlmConfig {

    private static final Logger logger = LoggerFactory.getLogger(LlmConfig.class);

    @Value("${app.ai.provider:ollama}")
    private String aiProvider;

    @Value("${app.gemini.api-key:}")
    private String geminiApiKey;

    @Value("${app.ollama.base-url:http://localhost:11434}")
    private String ollamaBaseUrl;

    @Value("${app.ollama.model-name:llama3.2}")
    private String ollamaModelName;

    @Bean
    public ChatLanguageModel chatLanguageModel() {
        if ("gemini".equalsIgnoreCase(aiProvider)) {
            if (geminiApiKey != null && !geminiApiKey.isBlank() && !geminiApiKey.equals("coloque_sua_chave_aqui")) {
                logger.info("LLM Provider: Google Gemini (gemini-1.5-flash)");
                return GoogleAiGeminiChatModel.builder()
                        .apiKey(geminiApiKey)
                        .modelName("gemini-1.5-flash")
                        .temperature(0.7)
                        .build();
            }
            logger.warn("app.ai.provider=gemini mas GEMINI_API_KEY ausente. Revertendo para Ollama local.");
        }

        logger.info("LLM Provider: Ollama Local (URL: {}, Modelo: {})", ollamaBaseUrl, ollamaModelName);
        return OllamaChatModel.builder()
                .baseUrl(ollamaBaseUrl)
                .modelName(ollamaModelName)
                .temperature(0.7)
                .build();
    }
}
