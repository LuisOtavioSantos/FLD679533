package com.example.rest.controllers;

import com.example.rest.dto.auth.AuthResponse;
import com.example.rest.dto.auth.LoginRequest;
import com.example.rest.dto.auth.RegisterRequest;
import com.example.rest.services.IAuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// Controller fino: apenas recebe a requisição e delega para o Service (Single Responsibility - SOLID)
@RestController
@RequestMapping("/auth")
@Tag(name = "Autenticação", description = "Endpoints de registro e login")
public class AuthController {

    private final IAuthService authService;

    public AuthController(IAuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    @Operation(summary = "Registrar novo cliente", description = "Cria um novo cliente com email e senha, retorna token JWT")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        try {
            AuthResponse response = authService.register(request);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            // Email já em uso
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/login")
    @Operation(summary = "Login", description = "Autentica com email e senha, retorna token JWT")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }
}
