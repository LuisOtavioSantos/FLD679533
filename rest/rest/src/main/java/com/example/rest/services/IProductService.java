package com.example.rest.services;

import com.example.rest.dto.ProductDTO;

import java.util.List;

// Interface do Service de Produto (Princípio Open/Closed do SOLID)
// Permite trocar a implementação sem modificar quem depende dela
public interface IProductService {
    List<ProductDTO> findAll();
    ProductDTO findById(Long id);
    ProductDTO create(ProductDTO productDTO, String email);
    ProductDTO update(ProductDTO productDTO);
    void delete(Long id);
    ProductDTO uploadImage(Long id, org.springframework.web.multipart.MultipartFile file);
}
