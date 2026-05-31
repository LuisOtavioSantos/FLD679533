package com.example.rest.services.ai;

import dev.langchain4j.model.chat.ChatLanguageModel;
import org.springframework.stereotype.Service;

@Service
public class GeminiAiService {

    private final ChatLanguageModel chatModel;

    public GeminiAiService(ChatLanguageModel chatModel) {
        this.chatModel = chatModel;
    }

    public String chat(String prompt) {
        if (chatModel == null) {
            return "Erro: O modelo de chat do LangChain4j não está disponível.";
        }
        
        try {
            return chatModel.generate(prompt);
        } catch (Exception e) {
            return "Erro ao comunicar com a inteligência artificial: " + e.getMessage();
        }
    }
}
