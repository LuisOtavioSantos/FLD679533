package com.example.rest.controllers;

import com.example.rest.dto.auth.AuthResponse;
import com.example.rest.dto.auth.LoginRequest;
import com.example.rest.dto.auth.RegisterRequest;
import com.example.rest.services.IAuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
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
    @Operation(summary = "Registrar novo cliente", description = "Cria um novo cliente com email e senha, retorna token JWT no cookie")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        try {
            AuthResponse response = authService.register(request);
            ResponseCookie cookie = createJwtCookie(response.getToken());
            // Anulamos o token do body por segurança, pois agora viaja no Cookie
            response.setToken(null);
            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, cookie.toString())
                    .body(response);
        } catch (IllegalArgumentException e) {
            // Email já em uso
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/login")
    @Operation(summary = "Login", description = "Autentica com email e senha, retorna token JWT no cookie")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            AuthResponse response = authService.login(request);
            ResponseCookie cookie = createJwtCookie(response.getToken());
            response.setToken(null);
            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, cookie.toString())
                    .body(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(org.springframework.http.HttpStatus.FORBIDDEN).body(java.util.Map.of("message", e.getMessage()));
        }
    }

    @PostMapping("/logout")
    @Operation(summary = "Logout", description = "Remove o cookie JWT")
    public ResponseEntity<Void> logout() {
        ResponseCookie cookie = ResponseCookie.from("jwt", "")
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(0) // Remove o cookie
                .sameSite("Strict")
                .build();
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .build();
    }

    @GetMapping("/verify")
    @Operation(summary = "Verifica E-mail", description = "Ativa a conta do cliente a partir do token gerado no cadastro")
    public ResponseEntity<String> verifyEmail(@RequestParam("token") String token) {
        try {
            authService.verifyEmail(token);
            // Em vez de retornar um JSON, podemos retornar um HTML básico para o navegador do usuário
            String html = "<html><head><meta charset='UTF-8'></head><body style='font-family:sans-serif; text-align:center; padding-top: 50px;'>" +
                          "<h1 style='color: green;'>E-mail verificado com sucesso!</h1>" +
                          "<p>Sua conta está ativa. Você já pode fazer login e realizar compras.</p>" +
                          "<a href='http://localhost:3000/login' style='padding: 10px 20px; background: #007bff; color: white; text-decoration: none; border-radius: 5px;'>Ir para o Login</a>" +
                          "</body></html>";
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_TYPE, "text/html; charset=UTF-8")
                    .body(html);
        } catch (IllegalArgumentException e) {
            String html = "<html><head><meta charset='UTF-8'></head><body style='font-family:sans-serif; text-align:center; padding-top: 50px;'>" +
                          "<h1 style='color: red;'>Falha na verificação</h1>" +
                          "<p>" + e.getMessage() + "</p>" +
                          "</body></html>";
            return ResponseEntity.badRequest()
                    .header(HttpHeaders.CONTENT_TYPE, "text/html; charset=UTF-8")
                    .body(html);
        }
    }

    @GetMapping("/me")
    @Operation(summary = "Obter dados do usuário logado", description = "Retorna os dados do usuário autenticado a partir do token JWT ativo")
    public ResponseEntity<AuthResponse> me(@org.springframework.security.core.annotation.AuthenticationPrincipal org.springframework.security.core.userdetails.UserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(org.springframework.http.HttpStatus.UNAUTHORIZED).build();
        }
        AuthResponse response = authService.getCurrentUser(userDetails.getUsername());
        return ResponseEntity.ok(response);
    }

    private ResponseCookie createJwtCookie(String token) {
        return ResponseCookie.from("jwt", token)
                .httpOnly(true)       // Proíbe acesso via Javascript (Mitiga XSS)
                .secure(false)        // Deve ser 'true' em Produção (HTTPS)
                .path("/")            // Cookie válido para todas as rotas da API
                .maxAge(36000)        // Duração do cookie em segundos
                .sameSite("Lax")      // Lax permite envio cross-origin em navegação top-level (necessário no dev: portas diferentes)
                .build();
    }
}

