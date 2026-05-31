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

import org.springframework.transaction.annotation.Transactional;

// Service de Autenticação (Princípio Single Responsibility do SOLID)
// Toda a lógica de registro e login fica aqui, não no Controller
@Service
@Transactional
public class AuthService implements IAuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    private final ClientRepository clientRepository;
    private final com.example.rest.repository.EmailVerificationTokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;

    public AuthService(ClientRepository clientRepository,
                       com.example.rest.repository.EmailVerificationTokenRepository tokenRepository,
                       PasswordEncoder passwordEncoder,
                       JwtTokenProvider jwtTokenProvider,
                       AuthenticationManager authenticationManager,
                       EmailService emailService) {
        this.clientRepository = clientRepository;
        this.tokenRepository = tokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
        this.authenticationManager = authenticationManager;
        this.emailService = emailService;
    }

    @Override
    public AuthResponse register(RegisterRequest request) {
        logger.info("Iniciando tentativa de registro para o email: {}", request.getEmail());
        
        if (clientRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email já está em uso: " + request.getEmail());
        }

        // Mesmo se a requisição for interceptada e enviar com role=Admin não permitimos o registro
        Role userRole;
        try {
            userRole = Role.valueOf(request.getRole().toUpperCase());
            if (userRole == Role.ADMIN) {
                throw new IllegalArgumentException("Não é permitido registrar um administrador publicamente.");
            }
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Role inválida. Valores permitidos: CLIENT, VENDOR");
        }

        Client client = Client.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .address(request.getAddress())
                .gender(request.getGender())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(userRole)
                .isEmailVerified(false) // Força falso no registro
                .build();

        clientRepository.save(client);
        
        // INÍCIO DA LÓGICA DE VERIFICAÇÃO DE E-MAIL
        String verificationToken = java.util.UUID.randomUUID().toString();
        com.example.rest.model.EmailVerificationToken emailToken = com.example.rest.model.EmailVerificationToken.builder()
                .token(verificationToken)
                .client(client)
                .expiryDate(java.time.LocalDateTime.now().plusHours(24))
                .build();
        tokenRepository.save(emailToken);
        
        // Disparo real de e-mail usando SMTP (Assíncrono)
        emailService.sendVerificationEmail(client.getEmail(), client.getFirstName(), verificationToken);
        //  FIM DA LÓGICA DE VERIFICAÇÃO DE E-MAIL

        // Retorna sem o JWT, pois a conta não está validada
        return AuthResponse.builder()
                .email(client.getEmail())
                .id(client.getId())
                .firstName(client.getFirstName())
                .lastName(client.getLastName())
                .role(client.getRole().name())
                .build();
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        logger.info("Tentativa de login para o email: {}", request.getEmail());

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
        } catch (org.springframework.security.authentication.DisabledException e) {
            logger.warn("Tentativa de login bloqueada: E-mail não verificado para {}", request.getEmail());
            throw new IllegalArgumentException("E-mail não confirmado. Por favor, verifique sua caixa de entrada.");
        } catch (org.springframework.security.core.AuthenticationException e) {
            throw new IllegalArgumentException("E-mail ou senha incorretos.");
        }

        String token = jwtTokenProvider.generateToken(request.getEmail());
        logger.info("Login bem-sucedido para o email: {}", request.getEmail());

        Client client = clientRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new com.example.rest.exception.ResourceNotFoundException("Client not found"));

        return AuthResponse.builder()
                .email(client.getEmail())
                .token(token)
                .id(client.getId())
                .firstName(client.getFirstName())
                .lastName(client.getLastName())
                .role(client.getRole().name())
                .build();
    }

    @Override
    public void verifyEmail(String token) {
        com.example.rest.model.EmailVerificationToken verificationToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new IllegalArgumentException("Token de verificação inválido."));

        if (verificationToken.isExpired()) {
            throw new IllegalArgumentException("Token de verificação expirado.");
        }

        Client client = verificationToken.getClient();
        client.setEmailVerified(true);
        clientRepository.save(client);

        // Delete o token após uso para manter limpo
        tokenRepository.delete(verificationToken);
    }

    @Override
    public AuthResponse getCurrentUser(String email) {
        Client client = clientRepository.findByEmail(email)
                .orElseThrow(() -> new com.example.rest.exception.ResourceNotFoundException("Cliente não encontrado com o e-mail: " + email));
        return AuthResponse.builder()
                .email(client.getEmail())
                .id(client.getId())
                .firstName(client.getFirstName())
                .lastName(client.getLastName())
                .role(client.getRole().name())
                .build();
    }
}

