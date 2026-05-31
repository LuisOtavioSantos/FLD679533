package com.example.rest.services;

import com.example.rest.annotation.LogExecution;
import com.example.rest.dto.ProductDTO;
import com.example.rest.exception.ResourceNotFoundException;
import com.example.rest.mapper.ProductMapper;
import com.example.rest.model.Product;
import com.example.rest.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.owasp.html.PolicyFactory;
import org.owasp.html.Sanitizers;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServices implements IProductService {

    @Autowired
    private ProductRepository repository;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private FileStorageService fileStorageService;

    private final Logger logger = LoggerFactory.getLogger(ProductServices.class);

    @LogExecution // Anotação Personalizada: loga automaticamente o tempo de execução (ver annotation/LogExecution.java)
    public List<ProductDTO> findAll() {
        logger.info("Find All Products");
        return repository.findAll().stream()
                .map(productMapper::toDTO)
                .collect(Collectors.toList());
    }

    public ProductDTO findById(Long id) {
        logger.info("Finding product with id: {}", id);
        Product product = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found!"));
        return productMapper.toDTO(product);
    }

    @LogExecution
    public ProductDTO create(ProductDTO productDTO, String email) {
        logger.info("Creating product: {} by vendor: {}", productDTO, email);
        
        com.example.rest.model.Client vendor = clientRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Vendor not found!"));
                
        // Regra de Negócio: Somente ROLE VENDOR ou ADMIN podem criar produtos
        if (vendor.getRole() != com.example.rest.model.Role.VENDOR && vendor.getRole() != com.example.rest.model.Role.ADMIN) {
            throw new IllegalArgumentException("Apenas vendedores podem criar produtos.");
        }

        Product product = productMapper.toEntity(productDTO);
        product.setVendor(vendor); // Associa o vendedor
        
        Product savedProduct = repository.save(product);
        return productMapper.toDTO(savedProduct);
    }

    public ProductDTO update(ProductDTO productDTO) {
        logger.info("Updating product: {}", productDTO);

        Product entity = repository.findById(productDTO.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found!"));

        productMapper.updateEntityFromDTO(productDTO, entity);

        Product updatedProduct = repository.save(entity);
        return productMapper.toDTO(updatedProduct);
    }

    public void delete(Long id) {
        logger.info("Deleting product: {}", id);
        Product entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found!"));
        repository.delete(entity);
    }

    @LogExecution
    public ProductDTO uploadImage(Long id, org.springframework.web.multipart.MultipartFile file) {
        logger.info("Uploading image for product id: {}", id);
        Product entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found!"));

        String path = fileStorageService.saveFile(file, "product-" + id);
        entity.setImageUrl(path);

        Product updatedProduct = repository.save(entity);
        return productMapper.toDTO(updatedProduct);
    }

    @Autowired
    private com.example.rest.repository.CommentRepository commentRepository;
    @Autowired
    private com.example.rest.repository.ClientRepository clientRepository;
    @Autowired
    private com.example.rest.repository.PurchaseRepository purchaseRepository;

    public List<com.example.rest.dto.comment.CommentResponse> getComments(Long productId) {
        return commentRepository.findByProductId(productId).stream().map(comment -> 
            com.example.rest.dto.comment.CommentResponse.builder()
                .id(comment.getId())
                .text(comment.getText())
                .rating(comment.getRating())
                .createdAt(comment.getCreatedAt())
                .clientId(comment.getClient().getId())
                .clientFirstName(comment.getClient().getFirstName())
                .clientLastName(comment.getClient().getLastName())
                .build()
        ).collect(Collectors.toList());
    }

    public com.example.rest.dto.comment.CommentResponse addComment(Long productId, com.example.rest.dto.comment.CommentRequest request, String email) {
        Product product = repository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found!"));
        com.example.rest.model.Client client = clientRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Client not found!"));

        // Regra de Negócio: Somente quem comprou o produto pode comentar
        boolean hasPurchased = purchaseRepository.existsByClientIdAndProductId(client.getId(), productId);
        if (!hasPurchased) {
            throw new IllegalArgumentException("Você precisa comprar o produto antes de avaliá-lo.");
        }

        // Sanitização contra XSS (Defesa em profundidade)
        // Permite formatação básica e links, mas remove scripts e tags perigosas
        PolicyFactory policy = Sanitizers.FORMATTING.and(Sanitizers.LINKS);
        String safeText = policy.sanitize(request.getText());

        com.example.rest.model.Comment comment = com.example.rest.model.Comment.builder()
                .text(safeText)
                .rating(request.getRating())
                .product(product)
                .client(client)
                .build();

        comment = commentRepository.save(comment);

        return com.example.rest.dto.comment.CommentResponse.builder()
                .id(comment.getId())
                .text(comment.getText())
                .rating(comment.getRating())
                .createdAt(comment.getCreatedAt())
                .clientId(client.getId())
                .clientFirstName(client.getFirstName())
                .clientLastName(client.getLastName())
                .build();
    }
}
