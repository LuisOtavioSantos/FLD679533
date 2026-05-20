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

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServices implements IProductService {

    @Autowired
    private ProductRepository repository;

    @Autowired
    private ProductMapper productMapper;

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
    public ProductDTO create(ProductDTO productDTO) {
        logger.info("Creating product: {}", productDTO);
        Product product = productMapper.toEntity(productDTO);
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
}
