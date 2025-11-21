package com.AU.GPA_CGPACALC.dto;

public class BulkSemesterDTO {
    private Integer number;
    private String description;
    private Integer mandatoryCount;
    private Integer electiveCount;
    private Long regulationId;
    private Long departmentId;

    public Integer getNumber() { return number; }
    public void setNumber(Integer number) { this.number = number; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Integer getMandatoryCount() { return mandatoryCount; }
    public void setMandatoryCount(Integer mandatoryCount) { this.mandatoryCount = mandatoryCount; }

    public Integer getElectiveCount() { return electiveCount; }
    public void setElectiveCount(Integer electiveCount) { this.electiveCount = electiveCount; }

    public Long getRegulationId() { return regulationId; }
    public void setRegulationId(Long regulationId) { this.regulationId = regulationId; }

    public Long getDepartmentId() { return departmentId; }
    public void setDepartmentId(Long departmentId) { this.departmentId = departmentId; }
}
