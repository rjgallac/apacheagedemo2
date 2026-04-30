package uk.co.sheffieldwebprogrammer.apacheagedemo.dto;

public class CompanyDto {
    private String name;
    private Long id;

    public CompanyDto(String name, Long id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}
