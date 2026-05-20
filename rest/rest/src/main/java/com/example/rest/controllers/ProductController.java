package com.example.rest.controllers;

import com.example.rest.dto.ProductDTO;
import com.example.rest.services.ProductServices;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product")
@Tag(name = "Produto", description = "Endpoints de gerenciamento de produtos")
public class ProductController {
    private final ProductServices service;

    public ProductController(ProductServices service) {
        this.service = service;
    }
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Listar produtos", description = "Retorna todos os produtos")
    public List<ProductDTO> findAll() {
        return service.findAll();
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Buscar produto", description = "Busca um produto pelo ID")
    public ProductDTO findById(@PathVariable("id") Long id) {
        return service.findById(id);
    }

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    // Retorna 201 (Created) em vez do padrão 200 (OK), indicando que um recurso foi criado com sucesso.
    @ResponseStatus(org.springframework.http.HttpStatus.CREATED)
    @Operation(summary = "Criar produto", description = "Cria um novo produto")
    // @Valid ativa a validação das anotações (@NotBlank, @NotNull) que colocamos dentro do ProductDTO
    public ProductDTO create(@jakarta.validation.Valid @RequestBody ProductDTO productDTO) {
        return service.create(productDTO);
    }

    @PutMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(summary = "Atualizar produto", description = "Atualiza os dados de um produto existente")
    public ProductDTO update(@jakarta.validation.Valid @RequestBody ProductDTO productDTO) {
        return service.update(productDTO);
    }

    @DeleteMapping(value = "/{id}")
    @Operation(summary = "Deletar produto", description = "Deleta um produto pelo ID")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
