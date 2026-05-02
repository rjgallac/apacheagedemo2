package uk.co.sheffieldwebprogrammer.apacheagedemo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import uk.co.sheffieldwebprogrammer.apacheagedemo.repository.GraphRepository;

@RestController
@RequestMapping("/api/graph")
public class GraphController {

    private final GraphRepository graphRepository;

    public GraphController(GraphRepository graphRepository) {
        this.graphRepository = graphRepository;
    }

    @GetMapping
    public String graphDb() {
        String[] nodeMatches = new String[] {"Company", "Person"};
        String[] relationMatches = new String[] {"EMPLOYED_AT"};
        return graphRepository.getAll(nodeMatches, relationMatches);
    } 
}
