package com.example.rest.services;

import com.example.rest.model.Person;
import com.example.rest.dto.PersonDTO;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;

@Service // torna a classe injetável
@Profile("dev")
public class PersonServices {

    private final AtomicLong counter = new AtomicLong();
    private final Logger logger = Logger.getLogger(PersonServices.class.getName());

    public List<PersonDTO> findAll(){
        logger.info("Find All Persons"); // O ideal é usar SLF4J Simple Logging Facade for Java-> Veja o UserService
        List<PersonDTO> peopleDTOs = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            // Internamente simulamos a entidade Person (Mock DB)
            Person person = mockPerson(i);
            // E convertemos para DTO antes de devolver ao Controller
            peopleDTOs.add(convertToDTO(person));
        }
        return peopleDTOs;

    }

    public PersonDTO findById(String id) {
        logger.info("Finding person with id: " + id);

        // Simulando a busca de uma ENTIDADE no banco
        Person person = new Person();
        person.setId(counter.incrementAndGet()); 
        person.setFirstName("João");
        person.setLastName("");
        person.setAddress("Cidade - Minas Gerais - Brasil");
        person.setGender("Masculino");

        // Convertendo e retornando o DTO
        return convertToDTO(person);
    }

    public PersonDTO create(PersonDTO personDTO) {
        logger.info("Creating person from DTO: " + personDTO.getFirstName());
        // Aqui nós converteríamos o DTO para Entidade (Person), salvaríamos no banco (Mock), 
        // e devolveríamos a Entidade convertida de volta para DTO.
        
        // Exemplo simplificado de como simular essa conversão (Mock):
        Person entity = convertToEntity(personDTO);
        entity.setId(counter.incrementAndGet()); // Simulando banco gerando ID
        
        return convertToDTO(entity);
    }

    public PersonDTO update(PersonDTO personDTO) {
        logger.info("Updating person from DTO: " + personDTO.getFirstName());
        // Simulando a conversão DTO -> Entity, salvamento, Entity -> DTO
        Person entity = convertToEntity(personDTO);
        return convertToDTO(entity);
    }

    public void delete(String id) {
        logger.info("Deleting person: " + id);
    }


    // -- MÉTODOS DE CONVERSÃO DTO <-> ENTITY (Pedagógicos para a Unidade 2) --
    // Esses métodos mostram visualmente a separação dos dados de negócio (Person)
    // dos dados que viajam na requisição (PersonDTO).
    private PersonDTO convertToDTO(Person person) {
        return new PersonDTO(
            person.getId(), 
            person.getFirstName(), 
            person.getLastName(), 
            person.getAddress(), 
            person.getGender()
        );
    }

    private Person convertToEntity(PersonDTO dto) {
        Person person = new Person();
        person.setId(dto.getId());
        person.setFirstName(dto.getFirstName());
        person.setLastName(dto.getLastName());
        person.setAddress(dto.getAddress());
        person.setGender(dto.getGender());
        return person;
    }

    private Person mockPerson(int i) {
        Person person = new Person();
        person.setId(counter.incrementAndGet()); // simulando o acesso ao banco e persistência
        person.setFirstName("João" + i);
        person.setLastName("das Couves" + i);
        person.setAddress("Outra Cidade - Minas Gerais - Brasil" + i);
        person.setGender("Masculino" + i);
        return person;
    }

}
