package com.example.rest.services;

import com.example.rest.dto.auth.AuthResponse;
import com.example.rest.dto.auth.LoginRequest;
import com.example.rest.dto.auth.RegisterRequest;

// Interface do Service de Autenticação (Princípio Single Responsibility do SOLID)
// Extrai a lógica de negócio que antes estava no AuthController
public interface IAuthService {
    AuthResponse register(RegisterRequest request);
    AuthResponse login(LoginRequest request);
}
