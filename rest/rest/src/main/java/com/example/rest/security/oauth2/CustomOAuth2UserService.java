package com.example.rest.security.oauth2;

import com.example.rest.model.Client;
import com.example.rest.model.Role;
import com.example.rest.repository.ClientRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;

@Service
@Transactional
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final ClientRepository clientRepository;
    private final PasswordEncoder passwordEncoder;

    //public CustomOAuth2UserService(ClientRepository clientRepository, @org.springframework.context.annotation.Lazy PasswordEncoder passwordEncoder) {
    public CustomOAuth2UserService(ClientRepository clientRepository, PasswordEncoder passwordEncoder) {
        this.clientRepository = clientRepository;
        this.passwordEncoder = passwordEncoder;
    }



    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        return processOAuth2User(oAuth2User);
    }

    private OAuth2User processOAuth2User(OAuth2User oAuth2User) {
        Map<String, Object> attributes = oAuth2User.getAttributes();
        String email = (String) attributes.get("email");

        if (email == null || email.trim().isEmpty()) {
            throw new OAuth2AuthenticationException("E-mail não fornecido pelo provedor OAuth2.");
        }

        String firstName = (String) attributes.get("given_name");
        String lastName = (String) attributes.get("family_name");
        String name = (String) attributes.get("name");

        if (firstName == null) {
            if (name != null) {
                String[] parts = name.split(" ", 2);
                firstName = parts[0];
                lastName = parts.length > 1 ? parts[1] : "";
            } else {
                firstName = email.split("@")[0];
                lastName = "";
            }
        }
        if (lastName == null) {
            lastName = "";
        }

        Client client = clientRepository.findByEmail(email).orElse(null);

        if (client != null) {
            // Conta já existe!
            // Se o e-mail não estava verificado localmente, ativamos agora de forma segura.
            if (!client.isEmailVerified()) {
                client.setEmailVerified(true);
                clientRepository.save(client);
            }
        } else {
            // Novo cadastro via Google OAuth2
            client = Client.builder()
                    .firstName(firstName)
                    .lastName(lastName)
                    .email(email)
                    .password(passwordEncoder.encode(UUID.randomUUID().toString())) // impede login clássico
                    .address("OAuth2 User")
                    .gender("Unknown")
                    .role(Role.CLIENT)
                    .isEmailVerified(true) // verificado automaticamente
                    .build();
            clientRepository.save(client);
        }

        return new DefaultOAuth2User(
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + client.getRole().name())),
                attributes,
                "email"
        );
    }
}
