package com.example.rest.controllers;

import com.example.rest.dto.PurchaseCreateDTO;
import com.example.rest.dto.PurchaseDTO;
import com.example.rest.services.PurchaseServices;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/purchase")
@Tag(name = "Compra", description = "Endpoints de gerenciamento de compras (Requer Autenticação JWT)")
@SecurityRequirement(name = "bearerAuth") // Exige token JWT no Swagger para todos os endpoints desta classe
public class PurchaseController {
    private final PurchaseServices service;

    public PurchaseController(PurchaseServices service) {
        this.service = service;
    }
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Listar compras", description = "Retorna todas as compras registradas")
    public List<PurchaseDTO> findAll() {
        return service.findAll();
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Buscar compra", description = "Busca uma compra pelo ID")
    public PurchaseDTO findById(@PathVariable("id") Long id) {
        return service.findById(id);
    }

    @GetMapping(value = "/client/{clientId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Compras do cliente", description = "Busca todas as compras de um cliente específico")
    public List<PurchaseDTO> findByClientId(@PathVariable("clientId") Long clientId) {
        return service.findByClientId(clientId);
    }

    @GetMapping(value = "/product/{productId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Compras do produto", description = "Busca todas as compras que incluem um produto específico")
    public List<PurchaseDTO> findByProductId(@PathVariable("productId") Long productId) {
        return service.findByProductId(productId);
    }

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @org.springframework.web.bind.annotation.ResponseStatus(org.springframework.http.HttpStatus.CREATED)
    @Operation(summary = "Criar compra", description = "Registra uma nova compra vinculando um cliente e um produto")
    public PurchaseDTO create(@jakarta.validation.Valid @RequestBody PurchaseCreateDTO purchaseDTO) {
        return service.create(purchaseDTO);
    }

    @DeleteMapping(value = "/{id}")
    @Operation(summary = "Deletar compra", description = "Deleta uma compra pelo ID")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
