package com.example.rest.services;

import com.example.rest.annotation.LogExecution;
import com.example.rest.dto.PurchaseCreateDTO;
import com.example.rest.dto.PurchaseDTO;
import com.example.rest.exception.ResourceNotFoundException;
import com.example.rest.mapper.PurchaseMapper;
import com.example.rest.model.Client;
import com.example.rest.model.Product;
import com.example.rest.model.Purchase;
import com.example.rest.repository.ClientRepository;
import com.example.rest.repository.ProductRepository;
import com.example.rest.repository.PurchaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PurchaseServices implements IPurchaseService {

    @Autowired
    private PurchaseRepository repository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private PurchaseMapper purchaseMapper;

    private final Logger logger = LoggerFactory.getLogger(PurchaseServices.class);

    public List<PurchaseDTO> findAll() {
        logger.info("Find All Purchases");
        return repository.findAll().stream()
                .map(purchaseMapper::toDTO)
                .collect(Collectors.toList());
    }

    public PurchaseDTO findById(Long id) {
        logger.info("Finding purchase with id: {}", id);
        Purchase purchase = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Purchase not found!"));
        return purchaseMapper.toDTO(purchase);
    }

    public List<PurchaseDTO> findByClientId(Long clientId) {
        logger.info("Finding purchases for client id: {}", clientId);
        clientRepository.findById(clientId)
                .orElseThrow(() -> new ResourceNotFoundException("Client not found!"));
        return repository.findByClientId(clientId).stream()
                .map(purchaseMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<PurchaseDTO> findByProductId(Long productId) {
        logger.info("Finding purchases for product id: {}", productId);
        productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found!"));
        return repository.findByProductId(productId).stream()
                .map(purchaseMapper::toDTO)
                .collect(Collectors.toList());
    }

    @LogExecution // Anotação Personalizada: loga automaticamente o tempo de execução
    public PurchaseDTO create(PurchaseCreateDTO createDTO) {
        logger.info("Creating purchase for client: {} and product: {}", createDTO.getClientId(), createDTO.getProductId());

        Client client = clientRepository.findById(createDTO.getClientId())
                .orElseThrow(() -> new ResourceNotFoundException("Client not found!"));
        Product product = productRepository.findById(createDTO.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found!"));

        Purchase purchase = Purchase.builder()
                .client(client)
                .product(product)
                .quantity(createDTO.getQuantity())
                .unitPrice(product.getPrice()) // Pega o preço atual do produto
                .build();

        Purchase savedPurchase = repository.save(purchase);
        return purchaseMapper.toDTO(savedPurchase);
    }

    public void delete(Long id) {
        logger.info("Deleting purchase: {}", id);
        Purchase entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Purchase not found!"));
        repository.delete(entity);
    }
}
