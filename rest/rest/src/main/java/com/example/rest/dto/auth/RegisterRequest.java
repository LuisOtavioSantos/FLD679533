package com.example.rest.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterRequest {
    
    @NotBlank(message = "O email é obrigatório")
    @Email(message = "Formato de email inválido")
    private String email;
    
    @NotBlank(message = "A senha é obrigatória")
    private String password;
    
    @NotBlank(message = "O primeiro nome é obrigatório")
    private String firstName;
    
    @NotBlank(message = "O sobrenome é obrigatório")
    private String lastName;
    
    private String address;
    private String gender;
}
