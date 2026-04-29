package uk.co.sheffieldwebprogrammer.apacheagedemo.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import org.springframework.http.MediaType;

import uk.co.sheffieldwebprogrammer.apacheagedemo.dto.CreatePersonDto;
import uk.co.sheffieldwebprogrammer.apacheagedemo.dto.CreatePersonRelationDto;
import uk.co.sheffieldwebprogrammer.apacheagedemo.dto.Person;
import uk.co.sheffieldwebprogrammer.apacheagedemo.repository.PersonRepository;

@RestController
@RequestMapping("/api/person")
public class PersonController {

    private final PersonRepository personRepository;

    public PersonController(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }
   
    @PostMapping
    public String createNode(@RequestBody CreatePersonDto createPersonDto) {
        return personRepository.createPerson(createPersonDto.getName());
    }

    @GetMapping
    public List<Person> listpeople() {
        return personRepository.getNodes();

    }
    

    @GetMapping("/create-edge")
    public String createEdge(
            @RequestParam String name1,
            @RequestParam String name2) {
        return personRepository.createRelation(name1, name2);
    }
    
    @DeleteMapping("/delete-all")
    public String deleteAll(){

        return personRepository.deleteAll();

    }
    @GetMapping("/get-all")
    public String getAll() {
        try {
            return personRepository.getAll();
        } catch (Exception e) {
            return "Error retrieving data: " + e.getMessage();
        }
    }

    @GetMapping(value = "/graph", produces = MediaType.APPLICATION_JSON_VALUE)
    public String graph() {
        return personRepository.getGraph();
    }

    @GetMapping(value = "/graph/db", produces = MediaType.APPLICATION_JSON_VALUE)
    public String graphDb() {
        return personRepository.getAll();
    }

    @DeleteMapping("/{id}")
    public String deleteNode(@PathVariable Long id) {
        personRepository.deletebyid(id);
        return "Node deleted successfully";
    }

    @PostMapping("/create-node-and-relation")
    public String createNodeAndRelation(@RequestBody CreatePersonRelationDto createPersonRelationDto) {
        personRepository.createNodeAndRelation(createPersonRelationDto.getName(), createPersonRelationDto.getManagerId());
        return "Node and relation created successfully";
    }

    @GetMapping("{id}")
    public Person getPersonById(@PathVariable Long id) {
        return personRepository.findById(id);
    }

}
