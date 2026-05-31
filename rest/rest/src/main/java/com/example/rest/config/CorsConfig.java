package com.example.rest.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuração Global de CORS (Cross-Origin Resource Sharing)
 *
 * CORS é um mecanismo de segurança do navegador que bloqueia requisições
 * de origens diferentes (ex: frontend em localhost:3000 chamando backend em
 * localhost:8090).
 *
 * Sem esta configuração, o frontend receberia erro:
 * "Access to XMLHttpRequest has been blocked by CORS policy"
 *
 * Referência: Aula3_3 - @CrossOrigin para comunicação front↔back
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {

        @Value("#{'${cors.allowed-origins:http://localhost:5173,http://localhost:3000,http://localhost:4200}'.split(',')}")
        private String[] allowedOrigins;

        private final RateLimitInterceptor rateLimitInterceptor;

        public CorsConfig(RateLimitInterceptor rateLimitInterceptor) {
                this.rateLimitInterceptor = rateLimitInterceptor;
        }

        @Override
        public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") // Aplica CORS a todos os endpoints
                                .allowedOrigins(allowedOrigins) // <--- Padrão Ouro: Lendo do arquivo de propriedades
                                .allowedMethods( // Métodos HTTP permitidos
                                                "GET",
                                                "POST",
                                                "PUT",
                                                "DELETE",
                                                "OPTIONS" // Preflight request
                                )
                                .allowedHeaders("*") // Permite todos os headers
                                .allowCredentials(true) // Permite envio de cookies/tokens JWT
                                .maxAge(3600); // Cache do preflight por 1 hora
        }

        @Override
        public void addInterceptors(org.springframework.web.servlet.config.annotation.InterceptorRegistry registry) {
                registry.addInterceptor(rateLimitInterceptor)
                        .addPathPatterns("/**") // Aplica a todas as rotas
                        .excludePathPatterns("/swagger-ui/**", "/v1/api-docs/**"); // Ignora rotas do Swagger
        }
}
