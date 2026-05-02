package uk.co.sheffieldwebprogrammer.apacheagedemo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import uk.co.sheffieldwebprogrammer.apacheagedemo.repository.TreeRepository;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping("/api/tree")
public class TreeController {

    private final TreeRepository treeRepository;

    public TreeController(TreeRepository treeRepository){
        this.treeRepository = treeRepository;
    }

    @GetMapping(produces = "application/json")
    public String getTree() {
        String[] nodeMatches = {"PERSON", "COMPANY", "DEPT"};
        String[] relationMatches = {"HAS_DEPT", "HAS_TEAM", "MEMBER_OF"};
        return treeRepository.getHierarchy(nodeMatches, relationMatches);
    }
    

}
