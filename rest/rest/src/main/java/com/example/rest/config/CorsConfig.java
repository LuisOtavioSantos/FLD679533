package com.example.rest.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuração Global de CORS (Cross-Origin Resource Sharing)
 *
 * CORS é um mecanismo de segurança do navegador que bloqueia requisições
 * de origens diferentes (ex: frontend em localhost:3000 chamando backend em localhost:8090).
 *
 * Sem esta configuração, o frontend receberia erro:
 * "Access to XMLHttpRequest has been blocked by CORS policy"
 *
 * Referência: Aula3_3 - @CrossOrigin para comunicação front↔back
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")                   // Aplica CORS a todos os endpoints
                .allowedOrigins("http://localhost:*",  // Permite qualquer porta local (desenvolvimento)
                        "http://127.0.0.1:*")
                .allowedMethods(                       // Métodos HTTP permitidos
                        "GET",
                        "POST",
                        "PUT",
                        "DELETE",
                        "OPTIONS"                      // Preflight request (navegador envia antes de POST/PUT/DELETE)
                )
                .allowedHeaders("*")                   // Permite todos os headers (Authorization, Content-Type, etc.)
                .allowCredentials(true)                 // Permite envio de cookies/tokens
                .maxAge(3600);                          // Cache do preflight por 1 hora (evita requests OPTIONS repetidos)
    }
}
