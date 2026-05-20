package com.example.rest.config;

import io.swagger.v3.oas.models.Components; // faz parte da dependencia webmvc
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApiSwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("REST API - Loja")
                        .description("API REST com autenticação JWT, documentação Swagger e mapeamento MapStruct")
                        .version("1.0.0"))
                // Define o esquema de segurança Bearer JWT
                .components(new Components()
                        .addSecuritySchemes("bearerAuth",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("Insira o token JWT aqui (sem o prefixo 'Bearer ')")))
                // Aplica o esquema globalmente (o cadeado aparece em todos os endpoints)
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"));
    }
}
