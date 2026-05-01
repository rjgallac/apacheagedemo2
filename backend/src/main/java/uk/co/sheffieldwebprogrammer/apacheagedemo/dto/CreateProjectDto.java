package uk.co.sheffieldwebprogrammer.apacheagedemo.dto;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/project")
public class CreateProjectDto {

    @PostMapping
    public void createProject(@RequestBody CreateProjectDto createProjectDto) {
    }

}
