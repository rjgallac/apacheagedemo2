package uk.co.sheffieldwebprogrammer.apacheagedemo.dto;

public class CreatePersonDto {
    private String name;

    public CreatePersonDto() {
    }

    public CreatePersonDto(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
