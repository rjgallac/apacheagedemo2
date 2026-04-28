package uk.co.sheffieldwebprogrammer.apacheagedemo.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import org.springframework.http.MediaType;

import uk.co.sheffieldwebprogrammer.apacheagedemo.dto.Person;
import uk.co.sheffieldwebprogrammer.apacheagedemo.repository.PersonRepository;

@RestController
public class TestController {

    private final PersonRepository personRepository;

    public TestController(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }
   
    @GetMapping("/create-node")
    public String createNode(@RequestParam String name) {
        return personRepository.createPerson(name);
        
    }

    @GetMapping("list-people")
    public List<Person> listpeople() {
        return personRepository.getNodes();

    }
    

    @GetMapping("/create-edge")
    public String createEdge(
            @RequestParam String name1,
            @RequestParam String name2) {
        return personRepository.createRelation(name1, name2);
    }

    @GetMapping("/test")
    public String test() {
        return "AGE Database Controller is running!";
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
}
