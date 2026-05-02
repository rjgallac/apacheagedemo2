package uk.co.sheffieldwebprogrammer.apacheagedemo.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import uk.co.sheffieldwebprogrammer.apacheagedemo.dto.EdgeDto;
import uk.co.sheffieldwebprogrammer.apacheagedemo.repository.EdgeRepository;

@RestController
@RequestMapping("/api/edge")
public class EdgeController {

    private final EdgeRepository edgeRepository;

    public EdgeController(EdgeRepository edgeRepository) {
        this.edgeRepository = edgeRepository;
    }


    @PostMapping
    public void createEdge(@RequestBody EdgeDto edgeDto) {
        edgeRepository.createEdit(edgeDto.getNodeId1(), edgeDto.getNodeId2(), edgeDto.getRelationshipType());
    }

}
