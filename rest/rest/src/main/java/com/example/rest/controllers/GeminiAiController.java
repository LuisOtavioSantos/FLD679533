package com.example.rest.controllers;

import com.example.rest.services.ai.GeminiAiService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ai/premium/gemini")
public class GeminiAiController {

    private final GeminiAiService geminiAiService;

    public GeminiAiController(GeminiAiService geminiAiService) {
        this.geminiAiService = geminiAiService;
    }

    /**
     * Endpoint isolado que acessa o Google Gemini usando LangChain4j.
     * Requer autenticação para acesso (poderia até cobrar créditos aqui no futuro).
     */
    @GetMapping("/chat")
    // @PreAuthorize("hasAnyRole('ADMIN', 'CLIENT')") // Segurança opcional via anotação (já protegemos no SecurityConfig)
    public ResponseEntity<String> chat(@RequestParam String prompt) {
        String response = geminiAiService.chat(prompt);
        return ResponseEntity.ok(response);
    }
}
