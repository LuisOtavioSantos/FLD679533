package com.example.rest.security;

import com.example.rest.repository.ClientRepository;
import com.example.rest.security.jwt.JwtTokenFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.config.Customizer;
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

import com.example.rest.security.oauth2.CustomOAuth2UserService;
import com.example.rest.security.oauth2.OAuth2AuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtTokenFilter jwtTokenFilter;
    private final ClientRepository clientRepository;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
    private final PasswordEncoder passwordEncoder;

    public SecurityConfig(JwtTokenFilter jwtTokenFilter, 
                          @Lazy ClientRepository clientRepository,
                          CustomOAuth2UserService customOAuth2UserService,
                          OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler,
                          PasswordEncoder passwordEncoder) {
        this.jwtTokenFilter = jwtTokenFilter;
        this.clientRepository = clientRepository;
        this.customOAuth2UserService = customOAuth2UserService;
        this.oAuth2AuthenticationSuccessHandler = oAuth2AuthenticationSuccessHandler;
        this.passwordEncoder = passwordEncoder;
    }



    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Ativa a configuração de CORS (baseada no CorsConfig) no Security Filter
                .cors(Customizer.withDefaults())
                // Desabilita CSRF (API stateless, sem cookies de sessão)
                .csrf(AbstractHttpConfigurer::disable)

                // Sessão stateless para requisições de API, mas criada IF_REQUIRED para o fluxo OAuth2
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))

                // Regras de autorização
                .authorizeHttpRequests(auth -> auth
                        // Endpoints públicos da nossa Aula 2
                        .requestMatchers("/person/**", "/math/**", "/hello/**", "/test/**").permitAll()

                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers("/login/oauth2/code/**").permitAll() // endpoint de callback do OAuth2
                        .requestMatchers("/swagger-ui/**", "/v1/api-docs/**", "/swagger-ui.html", "/swagger-resources/**", "/webjars/**").permitAll() // qualquer um pode ver o swagger
                        //.requestMatchers("/swagger-ui/**", "/v1/api-docs/**").authenticated() // Somente quem tem token JWT válido pode ver o Swagger
                        //.requestMatchers("/swagger-ui/**", "/v1/api-docs/**").hasRole("ADMIN") // Somente o ADMIN pode ver o Swagger

                        // Buscar um específico é autenticado, mas Listar todos ou Deletar requer ADMIN
                        .requestMatchers(HttpMethod.GET, "/user").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/user/**").hasRole("ADMIN")
                        .requestMatchers("/user/**").authenticated()

                        // Produtos e Imagens: Ver é público, mas Criar/Editar/Deletar requer ADMIN ou VENDOR
                        // TODO: USUÁRIO DO TIPO VENDEDOR SÓ PODE EDITAR/CRIAR/ATUALIZAR PRODUTOS DELE
                        .requestMatchers(HttpMethod.GET, "/product/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/uploads/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/product/*/comments").authenticated()
                        .requestMatchers("/product/**").hasAnyRole("ADMIN", "VENDOR")

                        // Compras: Requer estar logado
                        .requestMatchers("/purchase/**").authenticated()

                        // Qualquer outro endpoint requer autenticação
                        .anyRequest().authenticated()
                )

                // Configura o Login via OAuth2 (Google)
                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(customOAuth2UserService)
                        )
                        .successHandler(oAuth2AuthenticationSuccessHandler)
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
        authProvider.setPasswordEncoder(passwordEncoder);
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}

