package com.example.rest.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseCreateDTO {
    
    @NotNull(message = "O ID do cliente é obrigatório")
    private Long clientId;
    
    @NotNull(message = "O ID do produto é obrigatório")
    private Long productId;
    
    @NotNull(message = "A quantidade é obrigatória")
    @Min(value = 1, message = "A quantidade deve ser no mínimo 1")
    private Integer quantity;
}
