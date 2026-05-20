package com.example.rest.repository;

import com.example.rest.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client, Long> {

    // Busca por email para autenticação
    Optional<Client> findByEmail(String email);
}
