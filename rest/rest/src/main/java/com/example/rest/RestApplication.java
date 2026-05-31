package com.example.rest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.scheduling.annotation.EnableAsync;

// @Configuration (Permite definir e monitorar Beans de configuração)
// @EnableAutoConfiguration (O Spring Boot adivinha e configura coisas "automaticamente", como o banco de dados)
// @ComponentScan (Varre o pacote atual e subpacotes procurando por @RestController, @Service, @Repository, etc)
@SpringBootApplication
@EnableAsync
public class RestApplication {

    public static void main(String[] args) {
        SpringApplication.run(RestApplication.class, args);
    }

}
