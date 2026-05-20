package com.example.rest.mapper;

import com.example.rest.dto.PurchaseDTO;
import com.example.rest.model.Purchase;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PurchaseMapper {

    // Entity -> DTO (achata client e product para seus IDs)
    @Mapping(source = "client.id", target = "clientId")
    @Mapping(source = "product.id", target = "productId")
    PurchaseDTO toDTO(Purchase entity);
}
