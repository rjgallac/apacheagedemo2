package uk.co.sheffieldwebprogrammer.apacheagedemo.dto;

public class CreateCompanyRelationDto {

    private Long personId;
    private Long companyId;

    public CreateCompanyRelationDto() {
    }

    public CreateCompanyRelationDto(Long personId, Long companyId) {
        this.personId = personId;
        this.companyId = companyId;
    }

    public Long getPersonId() {
        return personId;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setPersonId(Long personId) {
        this.personId = personId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

}
