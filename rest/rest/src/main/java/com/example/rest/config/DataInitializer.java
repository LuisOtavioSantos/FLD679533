package com.example.rest.config;

import com.example.rest.dto.comment.CommentRequest;
import com.example.rest.model.Client;
import com.example.rest.model.Product;
import com.example.rest.model.Role;
import com.example.rest.repository.ClientRepository;
import com.example.rest.repository.ProductRepository;
import com.example.rest.services.ProductServices;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner loadData(ClientRepository clientRepository,
                                      ProductRepository productRepository,
                                      ProductServices productServices,
                                      PasswordEncoder passwordEncoder) {
        return args -> {
            // Criar produtos mock individualmente se não existirem (evita duplicações e permite atualizações dinâmicas)
            createProductIfNotExists(productRepository,
                    "Teclado Mecânico",
                    "Teclado mecânico RGB com switches azuis, ideal para digitação e jogos.",
                    250.00,
                    "Periféricos",
                    15,
                    "/uploads/keyboard.png");

            createProductIfNotExists(productRepository,
                    "Monitor 144Hz",
                    "Monitor Gamer de 24 polegadas, painel IPS, 144Hz e tempo de resposta de 1ms.",
                    1200.00,
                    "Hardware",
                    50,
                    "/uploads/monitor.png");

            createProductIfNotExists(productRepository,
                    "Mouse Gamer",
                    "Mouse ergonômico com 10.000 DPI ajustáveis e iluminação customizável.",
                    180.00,
                    "Periféricos",
                    100,
                    "/uploads/mouse.png");

            createProductIfNotExists(productRepository,
                    "Placa de Vídeo RTX",
                    "Placa de vídeo de última geração com suporte a Ray Tracing e DLSS 3.0.",
                    3500.00,
                    "Hardware",
                    10,
                    "/uploads/gpu.png");

            createProductIfNotExists(productRepository,
                    "Cadeira Gamer",
                    "Cadeira gamer ergonômica com inclinação de até 180 graus, suporte lombar e cervical.",
                    950.00,
                    "Móveis",
                    8,
                    "/uploads/chair.png");

            createProductIfNotExists(productRepository,
                    "Headset Gamer 7.1",
                    "Headset com som surround virtual 7.1, som 3D e microfone omnidirecional com redução de ruído.",
                    320.00,
                    "Periféricos",
                    30,
                    "/uploads/headset.png");

            createProductIfNotExists(productRepository,
                    "Notebook Gamer Intel i9",
                    "Notebook potente equipado com processador Intel i9 de 14ª geração, 32GB RAM e tela 165Hz.",
                    7800.00,
                    "Hardware",
                    5,
                    "/uploads/notebook.png");

            createProductIfNotExists(productRepository,
                    "SSD NVMe 1TB",
                    "SSD M.2 NVMe de altíssima velocidade com taxas de leitura de até 7000MB/s.",
                    450.00,
                    "Hardware",
                    40,
                    "/uploads/ssd.png");

            // Criar um usuário fake (somente se não existir)
            if (clientRepository.findByEmail("maria@rest.com").isEmpty()) {
                Client fakeClient = Client.builder()
                        .firstName("Maria")
                        .lastName("Silva")
                        .address("Rua Fictícia, 123")
                        .gender("FEMALE")
                        .email("maria@rest.com")
                        .password(passwordEncoder.encode("123456"))
                        .role(Role.CLIENT)
                        .isEmailVerified(true) // Usuário fake já nasce verificado
                        .build();
                clientRepository.save(fakeClient);
                System.out.println("Usuário fake 'maria@rest.com' gerado.");

                // Adicionar comentários fakes aos produtos existentes
                productRepository.findAll().forEach(product -> {
                    productServices.addComment(
                            product.getId(),
                            new CommentRequest("Produto excelente, recomendo!", 5),
                            "maria@rest.com"
                    );
                    productServices.addComment(
                            product.getId(),
                            new CommentRequest("Chegou rápido, mas achei o material um pouco frágil.", 3),
                            "maria@rest.com"
                    );
                });
                System.out.println("Comentários fakes gerados com sucesso!");
            } else {
                // Correção de dados legados: garante que a maria fake esteja verificada
                clientRepository.findByEmail("maria@rest.com").ifPresent(maria -> {
                    if (!maria.isEmailVerified()) {
                        maria.setEmailVerified(true);
                        clientRepository.save(maria);
                        System.out.println("Correção: maria@rest.com atualizada para isEmailVerified=true");
                    }
                });
            }

            // A lógica de atualizar imagens DEVE ficar de fora do if da Maria,
            // pois os produtos podem já existir sem foto (mesmo que a maria já exista)
            productRepository.findAll().forEach(product -> {
                if (product.getImageUrl() == null) {
                    if ("Teclado Mecânico".equals(product.getName())) product.setImageUrl("/uploads/keyboard.png");
                    else if ("Monitor 144Hz".equals(product.getName())) product.setImageUrl("/uploads/monitor.png");
                    else if ("Mouse Gamer".equals(product.getName())) product.setImageUrl("/uploads/mouse.png");
                    else if ("Placa de Vídeo RTX".equals(product.getName())) product.setImageUrl("/uploads/gpu.png");
                    else product.setImageUrl("/uploads/monitor.png"); // Fallback
                    productRepository.save(product);
                    System.out.println("Imagem atualizada para o produto: " + product.getName());
                }
            });
        };
    }

    private void createProductIfNotExists(ProductRepository productRepository,
                                          String name,
                                          String description,
                                          Double price,
                                          String category,
                                          Integer stock,
                                          String imageUrl) {
        if (!productRepository.existsByName(name)) {
            Product product = Product.builder()
                    .name(name)
                    .description(description)
                    .price(price)
                    .category(category)
                    .stock(stock)
                    .imageUrl(imageUrl)
                    .build();
            productRepository.save(product);
            System.out.println("Produto mock '" + name + "' semeado com sucesso!");
        }
    }
}
