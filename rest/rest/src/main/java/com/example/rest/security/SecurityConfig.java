package com.example.rest.security;

import com.example.rest.repository.ClientRepository;
import com.example.rest.security.jwt.JwtTokenFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtTokenFilter jwtTokenFilter;
    private final ClientRepository clientRepository;

    public SecurityConfig(JwtTokenFilter jwtTokenFilter, @Lazy ClientRepository clientRepository) {
        this.jwtTokenFilter = jwtTokenFilter;
        this.clientRepository = clientRepository;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Desabilita CSRF (API stateless, sem cookies de sessão)
                .csrf(AbstractHttpConfigurer::disable)

                // Sessão stateless (sem estado no servidor)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // Regras de autorização
                .authorizeHttpRequests(auth -> auth
                        // Endpoints públicos da nossa Aula 2
                        .requestMatchers("/person/**", "/math/**", "/hello/**", "/test/**").permitAll()

                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers("/swagger-ui/**", "/v1/api-docs/**", "/swagger-ui.html", "/swagger-resources/**", "/webjars/**").permitAll()
                        //.requestMatchers("/swagger-ui/**", "/v1/api-docs/**").authenticated() // Somente quem tem token JWT válido pode ver o Swagger
                        //.requestMatchers("/swagger-ui/**", "/v1/api-docs/**").hasRole("ADMIN") // Somente o ADMIN pode ver o Swagger

                        // Usuários: Buscar um específico é autenticado, mas Listar todos ou Deletar requer ADMIN
                        .requestMatchers(HttpMethod.GET, "/user").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/user/**").hasRole("ADMIN")
                        .requestMatchers("/user/**").authenticated()

                        // Produtos: Ver é público, mas Criar/Editar/Deletar requer ADMIN
                        .requestMatchers(HttpMethod.GET, "/product/**").permitAll()
                        .requestMatchers("/product/**").hasRole("ADMIN")

                        // Compras: Requer estar logado
                        .requestMatchers("/purchase/**").authenticated()

                        // Qualquer outro endpoint requer autenticação
                        .anyRequest().authenticated()
                )

                // Registra o filtro JWT antes do filtro padrão de autenticação
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return email -> clientRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Client não encontrado com email: " + email));
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
