package com.example.rest.mapper;

import com.example.rest.dto.ClientDTO;
import com.example.rest.model.Client;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface ClientMapper {

    // Entity -> DTO (campos com mesmo nome são mapeados automaticamente)
    ClientDTO toDTO(Client entity);

    @Mapping(target = "password", ignore = true)
    @Mapping(target = "purchases", ignore = true)
    @Mapping(target = "role", ignore = true)
    Client toEntity(ClientDTO dto);

    @Mapping(target = "password", ignore = true)
    @Mapping(target = "purchases", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "id", ignore = true)
    void updateEntityFromDTO(ClientDTO dto, @MappingTarget Client entity);
}
