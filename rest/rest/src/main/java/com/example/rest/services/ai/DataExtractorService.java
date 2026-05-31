package com.example.rest.services.ai;

import com.example.rest.dto.ai.ExtractionResult;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.spring.AiService;
import dev.langchain4j.service.spring.AiServiceWiringMode;

@AiService(wiringMode = AiServiceWiringMode.EXPLICIT, chatModel = "chatLanguageModel")
public interface DataExtractorService {

    @SystemMessage({
        "Você é um especialista em extração de dados.",
        "Extraia as informações do texto livre fornecido e retorne APENAS os dados estruturados.",
        "Se houver propriedade sentimentoDoCliente, use uma destas opções: POSITIVO, NEGATIVO, NEUTRO, URGENTE.",
        "Se houver propriedade horario, utilize o formato padrão do Brasil",
        "Se houver propriedade email, utilize a chave email"
    })
    ExtractionResult extrairDadosAgendamento(@UserMessage String textoLivre);
}
