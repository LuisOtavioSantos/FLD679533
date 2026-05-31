package com.example.rest.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuração para servir arquivos estáticos (imagens de produtos).
 *
 * Mapeia a URL /uploads/** para a pasta física ./uploads/ no disco.
 * Equivalente simplificado ao que Amazon faz com S3 + CloudFront.
 *
 * Exemplo: GET http://localhost:8090/uploads/product-1-abc123.jpg
 *          → retorna o arquivo de ./uploads/product-1-abc123.jpg
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:./uploads/")
                .setCachePeriod(3600); // Cache de 1 hora no navegador
    }
}
