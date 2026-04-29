package uk.co.sheffieldwebprogrammer.apacheagedemo.dto;

public class CreatePersonRelationDto {
    private String name;
    private Long managerId;

    public CreatePersonRelationDto() {
    }

    public CreatePersonRelationDto(String name, Long managerId) {
        this.name = name;
        this.managerId = managerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getManagerId() {
        return managerId;
    }

    public void setManagerId(Long managerId) {
        this.managerId = managerId;
    }
}
