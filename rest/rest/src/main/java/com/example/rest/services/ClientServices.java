package com.example.rest.services;

import com.example.rest.dto.ClientDTO;
import com.example.rest.exception.ResourceNotFoundException;
import com.example.rest.mapper.ClientMapper;
import com.example.rest.model.Client;
import com.example.rest.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClientServices implements IClientService {

    @Autowired
    private ClientRepository repository;

    @Autowired
    private ClientMapper clientMapper;

    private final Logger logger = LoggerFactory.getLogger(ClientServices.class);

    public List<ClientDTO> findAll() {
        logger.info("Find All Clients");
        return repository.findAll().stream()
                .map(clientMapper::toDTO)
                .collect(Collectors.toList());
    }

    public ClientDTO findById(Long id) {
        logger.info("Finding client with id: {}", id);
        // O findById retorna um Optional (uma "caixa" que pode ou não conter o Cliente).
        // O orElseThrow abre a caixa: se tiver o cliente, ele retorna; se estiver vazia, lança o erro.
        Client client = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client not found!"));
        return clientMapper.toDTO(client);
    }

    // A criação (create) agora acontece no AuthController através do processo de registro

    public ClientDTO update(ClientDTO clientDTO) {
        logger.info("Updating client: {}", clientDTO);
        
        Client entity = repository.findById(clientDTO.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Client not found!"));

        // Atualiza a entidade existente com os dados do DTO
        clientMapper.updateEntityFromDTO(clientDTO, entity);

        Client updatedClient = repository.save(entity);
        return clientMapper.toDTO(updatedClient);
    }

    public void delete(Long id) {
        logger.info("Deleting client: {}", id);
        Client entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client not found!"));
        repository.delete(entity);
    }

    public ClientDTO becomeVendor(String email) {
        logger.info("Upgrading client {} to VENDOR", email);
        Client entity = repository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Client not found!"));
        
        entity.setRole(com.example.rest.model.Role.VENDOR);
        Client updatedClient = repository.save(entity);
        return clientMapper.toDTO(updatedClient);
    }
}
