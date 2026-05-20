package com.example.rest.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.rest.model.Client;
import com.example.rest.model.Role;
import com.example.rest.repository.ClientRepository;

@Configuration
public class DataInitializerConfig {

    @Value("${app.admin.email}")
    private String adminEmail;

    @Value("${app.admin.password}")
    private String adminPassword;

    @Bean
    public CommandLineRunner setupAdmin(ClientRepository repository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (repository.findByEmail(adminEmail).isEmpty()) {
                Client admin = Client.builder()
                        .firstName("Admin")
                        .lastName("Sistema")
                        .email(adminEmail)
                        .password(passwordEncoder.encode(adminPassword))
                        .address("Rua do Admin, 1")
                        .gender("Outro")
                        .role(Role.ADMIN)
                        .build();
                repository.save(admin);
                System.out.println("Usuário ADMINISTRADOR criado com sucesso: " + adminEmail + " / " + adminPassword);
            }
        };
    }

    @Bean
    public CommandLineRunner testConnection(JdbcTemplate jdbcTemplate) {
        return args -> {
            try {
                jdbcTemplate.execute("SELECT 1");
                System.out.println("Conexão com o banco de dados realizada com sucesso!");
            } catch (Exception e) {
                System.err.println("Falha na conexão com o banco de dados: " + e.getMessage());
            }
        };
    }
}
