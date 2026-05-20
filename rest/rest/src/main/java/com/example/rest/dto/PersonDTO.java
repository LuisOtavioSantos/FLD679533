package com.example.rest.dto;

import jakarta.validation.constraints.NotBlank;
import java.io.Serializable;

public class PersonDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    
    @NotBlank(message = "O primeiro nome é obrigatório")
    private String firstName;
    
    private String lastName;
    private String address;
    private String gender;

    // Construtor Padrão
    public PersonDTO() {
    }

    public PersonDTO(Long id, String firstName, String lastName, String address, String gender) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.gender = gender;
    }

    // GETTERS e SETTERS
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
}
