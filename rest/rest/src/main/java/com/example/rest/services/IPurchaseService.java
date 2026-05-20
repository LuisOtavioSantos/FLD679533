package com.example.rest.services;

import com.example.rest.dto.PurchaseCreateDTO;
import com.example.rest.dto.PurchaseDTO;

import java.util.List;

// Interface do Service de Compra (Princípio Open/Closed do SOLID)
public interface IPurchaseService {
    List<PurchaseDTO> findAll();
    PurchaseDTO findById(Long id);
    List<PurchaseDTO> findByClientId(Long clientId);
    List<PurchaseDTO> findByProductId(Long productId);
    PurchaseDTO create(PurchaseCreateDTO createDTO);
    void delete(Long id);
}
