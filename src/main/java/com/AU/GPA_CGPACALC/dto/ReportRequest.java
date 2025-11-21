package com.AU.GPA_CGPACALC.dto;

public class ReportRequest {
    private Long regulationId;
    private Long departmentId;
    private Integer semester;

    public ReportRequest() {}

    public Long getRegulationId() { return regulationId; }
    public void setRegulationId(Long regulationId) { this.regulationId = regulationId; }
    public Long getDepartmentId() { return departmentId; }
    public void setDepartmentId(Long departmentId) { this.departmentId = departmentId; }
    public Integer getSemester() { return semester; }
    public void setSemester(Integer semester) { this.semester = semester; }
}
