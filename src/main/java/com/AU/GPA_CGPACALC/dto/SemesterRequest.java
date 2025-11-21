package com.AU.GPA_CGPACALC.dto;

public class SemesterRequest {
    private Long id;
    private Integer number;
    private String description;
    private Long regulationId;
    private Long departmentId;
    private Integer mandatoryCount;
    private Integer electiveCount;
    private Boolean active;

    public SemesterRequest() {}

    // getters/setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Integer getNumber() { return number; }
    public void setNumber(Integer number) { this.number = number; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Long getRegulationId() { return regulationId; }
    public void setRegulationId(Long regulationId) { this.regulationId = regulationId; }
    public Long getDepartmentId() { return departmentId; }
    public void setDepartmentId(Long departmentId) { this.departmentId = departmentId; }
    public Integer getMandatoryCount() { return mandatoryCount; }
    public void setMandatoryCount(Integer mandatoryCount) { this.mandatoryCount = mandatoryCount; }
    public Integer getElectiveCount() { return electiveCount; }
    public void setElectiveCount(Integer electiveCount) { this.electiveCount = electiveCount; }
    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }
}
