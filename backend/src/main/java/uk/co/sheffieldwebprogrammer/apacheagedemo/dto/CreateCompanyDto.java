package uk.co.sheffieldwebprogrammer.apacheagedemo.dto;

public class CreateCompanyDto {
    private String name;

    public CreateCompanyDto() {
    }

    public CreateCompanyDto(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
