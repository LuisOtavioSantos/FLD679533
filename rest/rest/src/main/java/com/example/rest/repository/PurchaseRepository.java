package com.example.rest.repository;

import com.example.rest.model.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PurchaseRepository extends JpaRepository<Purchase, Long> {

    // Busca todas as compras de um cliente
    List<Purchase> findByClientId(Long clientId);

    // Busca todas as compras de um produto
    List<Purchase> findByProductId(Long productId);

    // Verifica se um cliente comprou um produto
    boolean existsByClientIdAndProductId(Long clientId, Long productId);
}
