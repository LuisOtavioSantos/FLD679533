package com.example.rest.controllers;

import com.example.rest.dto.ai.ExtractionResult;
import com.example.rest.services.ai.AiAssistantService;
import com.example.rest.services.ai.DataExtractorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
@Tag(name = "AI", description = "Endpoints de demonstração de Integração com Ollama (LLM Local)")
public class AiController {

    private final AiAssistantService assistantService;
    private final DataExtractorService extractorService;

    @GetMapping("/chat")
    @Operation(summary = "Conversar com a IA", description = "Envia uma pergunta simples para a IA e recebe a resposta em texto.")
    public String chat(@RequestParam String prompt) {
        return assistantService.responderPergunta(prompt);
    }

    @PostMapping("/extract")
    @Operation(summary = "Extrair Dados Estruturados", description = "Envia um texto livre longo e a IA devolve um JSON mastigado com os dados solicitados.")
    public ExtractionResult extractData(@RequestBody String textoLivre) {
        return extractorService.extrairDadosAgendamento(textoLivre);
    }
}
