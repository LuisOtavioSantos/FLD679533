package com.example.rest.mapper;

import com.example.rest.dto.ProductDTO;
import com.example.rest.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    // Entity -> DTO
    ProductDTO toDTO(Product entity);

    // DTO -> Entity
    @Mapping(target = "purchases", ignore = true)
    Product toEntity(ProductDTO dto);

    // Atualiza entidade existente com dados do DTO
    @Mapping(target = "purchases", ignore = true)
    @Mapping(target = "id", ignore = true)
    void updateEntityFromDTO(ProductDTO dto, @MappingTarget Product entity);
}
