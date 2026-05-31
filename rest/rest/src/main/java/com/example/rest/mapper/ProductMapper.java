package com.example.rest.mapper;

import com.example.rest.dto.ProductDTO;
import com.example.rest.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    // Entity -> DTO
    @Mapping(target = "vendorId", source = "vendor.id")
    @Mapping(target = "vendorName", source = "vendor.firstName")
    ProductDTO toDTO(Product entity);

    // DTO -> Entity
    @Mapping(target = "purchases", ignore = true)
    @Mapping(target = "vendor", ignore = true) // vendor must be set manually by the service
    Product toEntity(ProductDTO dto);

    // Atualiza entidade existente com dados do DTO
    @Mapping(target = "purchases", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "vendor", ignore = true) // vendor can't be changed during update by DTO
    void updateEntityFromDTO(ProductDTO dto, @MappingTarget Product entity);
}
