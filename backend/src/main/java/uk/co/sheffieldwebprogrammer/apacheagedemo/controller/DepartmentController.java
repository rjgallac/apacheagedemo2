package uk.co.sheffieldwebprogrammer.apacheagedemo.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import uk.co.sheffieldwebprogrammer.apacheagedemo.dto.CreateDepartmentDto;
import uk.co.sheffieldwebprogrammer.apacheagedemo.dto.CreateDepartmentLinkDto;
import uk.co.sheffieldwebprogrammer.apacheagedemo.dto.DepartmentDto;
import uk.co.sheffieldwebprogrammer.apacheagedemo.repository.DepartmentRepository;

@RestController
@RequestMapping("/api/department")
public class DepartmentController {


    private final DepartmentRepository departmentRepository;

    public DepartmentController(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    @GetMapping
    public List<DepartmentDto> getAllDepartments() {
        return departmentRepository.getAllDepartments();
    }
    
    @PostMapping
    public void createDepartment(@RequestBody CreateDepartmentDto createDepartmentDto) {
        departmentRepository.createDepartment(createDepartmentDto.getName());
    }

    @DeleteMapping("/{id}")
    public String deleteDepartment(@PathVariable Long id) {
        return departmentRepository.deleteDepartment(id);
    }

    @PostMapping("/create-link")
    public void createLink(@RequestBody CreateDepartmentLinkDto createDepartmentLinkDto) {
        departmentRepository.createLink(createDepartmentLinkDto.getDepartmentId(), createDepartmentLinkDto.getCompanyId());
    }
    
}
