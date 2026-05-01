package uk.co.sheffieldwebprogrammer.apacheagedemo.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import uk.co.sheffieldwebprogrammer.apacheagedemo.dto.CreateDepartmentDto;

@RestController
@RequestMapping("/api/department")
public class DepartmentController {

    @PostMapping
    public void createDepartment(@RequestBody CreateDepartmentDto createDepartmentDto) {
    }
    
}
