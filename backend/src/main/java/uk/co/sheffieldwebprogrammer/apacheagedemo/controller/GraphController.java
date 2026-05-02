package uk.co.sheffieldwebprogrammer.apacheagedemo.controller;

import java.util.stream.Stream;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import uk.co.sheffieldwebprogrammer.apacheagedemo.model.NodeEnum;
import uk.co.sheffieldwebprogrammer.apacheagedemo.model.RelationEnum;
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
        if (nodeFilter == null || nodeFilter.length == 0) {
            nodeFilter = Stream.of(NodeEnum.values())
                .map(Enum::name)
                .toArray(String[]::new) ;
        }
        if (relationFilter == null || relationFilter.length == 0) {
            relationFilter = Stream.of(RelationEnum.values())
            .map(Enum::name)
            .toArray(String[]::new);
        }
        return graphRepository.getAll(nodeFilter, relationFilter);
    } 
}
