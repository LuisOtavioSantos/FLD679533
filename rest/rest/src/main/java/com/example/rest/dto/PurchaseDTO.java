package com.example.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PurchaseDTO {
    private Long id;
    private Long clientId;
    private Long productId;
    private Integer quantity;
    private Double unitPrice;
    private Double totalPrice;
    private LocalDateTime purchaseDate;
}
