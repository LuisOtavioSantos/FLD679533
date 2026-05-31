package com.example.rest.controllers;

import com.example.rest.dto.PersonDTO;
import com.example.rest.services.PersonServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/person")
@Profile("dev")
public class PersonController {

    //private PersonServices service = new PersonServices(); // Sem o annotation @Service
    
    // O @Autowired diz ao Spring: "Por favor, instancie um objeto PersonServices e injete aqui para mim".
    // Isso evita que precisemos usar a palavra "new" manualmente, mantendo o código desacoplado.
    @Autowired
    private PersonServices service; // Só é possível por causa do annotation @Service e @Autowired

    @RequestMapping(
            method =  RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public List<PersonDTO> findAll() {
        return service.findAll();
    }


    @RequestMapping(value = "/{id}",
            method =  RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public PersonDTO findById(@PathVariable("id") String id) {
        return service.findById(id);
    }


    @RequestMapping(
            method =  RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @org.springframework.web.bind.annotation.ResponseStatus(org.springframework.http.HttpStatus.CREATED)
    public PersonDTO create(@jakarta.validation.Valid @RequestBody PersonDTO personDTO) {
        // O Controller agora recebe e devolve apenas o DTO.
        return service.create(personDTO);
    }

    @RequestMapping(value = "/{id}",
            method =  RequestMethod.PUT,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public PersonDTO update(@jakarta.validation.Valid @RequestBody PersonDTO personDTO) {
        // O Controller repassa o DTO para o service tratar
        return service.update(personDTO);
    }


    @RequestMapping(value = "/{id}",
            method =  RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public void delete(@PathVariable("id") String id) {
        service.delete(id);
    }


}
