package com.example.rest.services;

import com.example.rest.dto.auth.AuthResponse;
import com.example.rest.dto.auth.LoginRequest;
import com.example.rest.dto.auth.RegisterRequest;
import com.example.rest.model.Client;
import com.example.rest.model.Role;
import com.example.rest.repository.ClientRepository;
import com.example.rest.security.jwt.JwtTokenProvider;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// Service de Autenticação (Princípio Single Responsibility do SOLID)
// Toda a lógica de registro e login fica aqui, não no Controller
@Service
public class AuthService implements IAuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    private final ClientRepository clientRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;

    public AuthService(ClientRepository clientRepository,
                       PasswordEncoder passwordEncoder,
                       JwtTokenProvider jwtTokenProvider,
                       AuthenticationManager authenticationManager) {
        this.clientRepository = clientRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public AuthResponse register(RegisterRequest request) {
        logger.info("Iniciando tentativa de registro para o email: {}", request.getEmail());
        logger.debug("Dados recebidos no registro: firstName={}, lastName={}, address={}, gender={}",
                request.getFirstName(), request.getLastName(), request.getAddress(), request.getGender());

        // Verifica se email já existe
        if (clientRepository.findByEmail(request.getEmail()).isPresent()) {
            logger.warn("Falha no registro: O email {} já está em uso no banco de dados.", request.getEmail());
            throw new IllegalArgumentException("Email já está em uso: " + request.getEmail());
        }

        // Cria o Client com senha criptografada (SEMPRE como USER no registro público)
        Client client = Client.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .address(request.getAddress())
                .gender(request.getGender())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();

        clientRepository.save(client);
        logger.info("Cliente salvo com sucesso no banco de dados com ID: {}", client.getId());

        // Gera token JWT
        String token = jwtTokenProvider.generateToken(client.getEmail());
        logger.info("Token JWT gerado com sucesso para o email: {}", client.getEmail());

        /*
         * EXEMPLO: Se quisesse que o registro NÃO retornasse o token:
         * 1. Mude o retorno para uma String ou outro DTO
         * 2. return "Conta criada com sucesso! Faça login para continuar.";
         */
        return AuthResponse.builder()
                .email(client.getEmail())
                .token(token)
                .build();
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        logger.info("Tentativa de login para o email: {}", request.getEmail());

        // Autentica com Spring Security
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        // Se chegou aqui, autenticação foi bem-sucedida
        String token = jwtTokenProvider.generateToken(request.getEmail());
        logger.info("Login bem-sucedido para o email: {}", request.getEmail());

        return AuthResponse.builder()
                .email(request.getEmail())
                .token(token)
                .build();
    }
}
