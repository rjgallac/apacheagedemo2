package uk.co.sheffieldwebprogrammer.apacheagedemo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import uk.co.sheffieldwebprogrammer.apacheagedemo.dto.CompanyDto;
import uk.co.sheffieldwebprogrammer.apacheagedemo.dto.CreateCompanyDto;
import uk.co.sheffieldwebprogrammer.apacheagedemo.dto.CreateCompanyRelationDto;
import uk.co.sheffieldwebprogrammer.apacheagedemo.repository.CompanyRepository;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/company")
public class CompanyController {

    private final CompanyRepository companyRepository;

    public CompanyController(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    @GetMapping
    public List<CompanyDto> getCompanies() {
        return companyRepository.getAllCompanies();
    }

    @PostMapping
    public String postMethodName(@RequestBody CreateCompanyDto createCompanyDto) {
        return companyRepository.createCompany(createCompanyDto.getName());
    }

    @PostMapping("/create-edge")
    public String createEdge(@RequestBody CreateCompanyRelationDto createCompanyRelationDto) {
        return companyRepository.createRelation(createCompanyRelationDto.getPersonId(), createCompanyRelationDto.getCompanyId());
    }
    
}
