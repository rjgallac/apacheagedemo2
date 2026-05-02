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

    @GetMapping(produces = "application/json")
    public String graphDb(String[] nodeFilter, String[] relationFilter) {
        if (nodeFilter == null) {
            nodeFilter = new String[] {"Company", "Person", "Project", "Team", "Department"};
        }
        if (relationFilter == null) {
            relationFilter = new String[] {"EMPLOYED_AT", "WORKS_ON", "MANAGES", "MEMBER_OF"};
        }
        return graphRepository.getAll(nodeFilter, relationFilter);
    } 
}
