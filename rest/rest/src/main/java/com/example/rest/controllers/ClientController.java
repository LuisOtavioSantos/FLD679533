package com.example.rest.controllers;

import com.example.rest.dto.ClientDTO;
import com.example.rest.services.ClientServices;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@Tag(name = "Cliente", description = "Endpoints de gerenciamento de clientes")
public class ClientController {

    @Autowired
    private ClientServices service;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Listar clientes", description = "Retorna todos os clientes")
    public List<ClientDTO> findAll() {
        return service.findAll();
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Buscar cliente", description = "Busca um cliente pelo ID")
    public ClientDTO findById(@PathVariable("id") Long id) {
        return service.findById(id);
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Atualizar cliente", description = "Atualiza os dados de um cliente existente")
    public ClientDTO update(@jakarta.validation.Valid @RequestBody ClientDTO clientDTO) {
        return service.update(clientDTO);
    }

    @DeleteMapping(value = "/{id}")
    @Operation(summary = "Deletar cliente", description = "Deleta um cliente pelo ID")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(value = "/become-vendor", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Tornar Vendedor", description = "Altera o perfil do usuário logado para VENDEDOR")
    public ClientDTO becomeVendor(@org.springframework.security.core.annotation.AuthenticationPrincipal org.springframework.security.core.userdetails.UserDetails userDetails) {
        return service.becomeVendor(userDetails.getUsername());
    }
}
