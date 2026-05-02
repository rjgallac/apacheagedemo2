package uk.co.sheffieldwebprogrammer.apacheagedemo.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import uk.co.sheffieldwebprogrammer.apacheagedemo.dto.NodeDto;
import uk.co.sheffieldwebprogrammer.apacheagedemo.repository.NodeRepository;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/api/node")
public class NodeController {

    private final NodeRepository nodeRepository;

    public NodeController(NodeRepository nodeRepository) {
        this.nodeRepository = nodeRepository;
    }

    @GetMapping
    public List<NodeDto> getAll() {
        return nodeRepository.getAllNodes();
    }

    @PostMapping
    public String createNode(@RequestBody NodeDto createNodeDto) {
        return nodeRepository.createNode(createNodeDto.getName(), createNodeDto.getType());
    }

    @DeleteMapping("/{id}")
    public String deleteNode(@PathVariable Long id) {
        return nodeRepository.deleteNode(id);
    }
}
