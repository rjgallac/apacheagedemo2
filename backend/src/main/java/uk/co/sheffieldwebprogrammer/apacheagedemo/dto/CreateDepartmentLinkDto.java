package uk.co.sheffieldwebprogrammer.apacheagedemo.dto;

public class CreateDepartmentLinkDto {
    private Long departmentId;
    private Long companyId;

    public CreateDepartmentLinkDto() {
    }

    public CreateDepartmentLinkDto(Long departmentId, Long companyId) {
        this.departmentId = departmentId;
        this.companyId = companyId;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }
    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

}
