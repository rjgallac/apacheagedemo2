package uk.co.sheffieldwebprogrammer.apacheagedemo.dto;

public class CreateTeamDto {

    private String name;

    public CreateTeamDto() {
    }

    public CreateTeamDto(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
