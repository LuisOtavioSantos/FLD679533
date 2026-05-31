package com.example.rest.services.ai;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.spring.AiService;
import dev.langchain4j.service.spring.AiServiceWiringMode;

@AiService(wiringMode = AiServiceWiringMode.EXPLICIT, chatModel = "chatLanguageModel")
public interface AiAssistantService {

    @SystemMessage({
        "Você é um assistente virtual de uma empresa de tecnologia.",
        "Responda sempre de forma sucinta e em português do Brasil.",
        "Se não souber a resposta, diga que precisa consultar um especialista."
    })
    String responderPergunta(@UserMessage String pergunta);
}
