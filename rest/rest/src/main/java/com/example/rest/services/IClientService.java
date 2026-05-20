package com.example.rest.services;

import com.example.rest.dto.ClientDTO;

import java.util.List;

// Interface do Service de Cliente (Princípio Open/Closed do SOLID)
public interface IClientService {
    List<ClientDTO> findAll();
    ClientDTO findById(Long id);
    ClientDTO update(ClientDTO clientDTO);
    void delete(Long id);
}
