package com.example.rest.dto.comment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentResponse {
    private Long id;
    private String text;
    private Integer rating;
    private LocalDateTime createdAt;
    
    // Dados do usuário (simplificados)
    private Long clientId;
    private String clientFirstName;
    private String clientLastName;
}
