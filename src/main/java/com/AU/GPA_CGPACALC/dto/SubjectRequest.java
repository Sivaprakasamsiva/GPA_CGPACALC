package com.AU.GPA_CGPACALC.dto;

public class SubjectRequest {
    private Long id;
    private String code;
    private String name;
    private Double credits;
    private String type; // CORE | LAB | ELECTIVE
    private Boolean isElective;
    private Long regulationId;
    private Long departmentId;
    private Integer semester;

    public SubjectRequest() {}

    // getters/setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Double getCredits() { return credits; }
    public void setCredits(Double credits) { this.credits = credits; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public Boolean getIsElective() { return isElective; }
    public void setIsElective(Boolean isElective) { this.isElective = isElective; }
    public Long getRegulationId() { return regulationId; }
    public void setRegulationId(Long regulationId) { this.regulationId = regulationId; }
    public Long getDepartmentId() { return departmentId; }
    public void setDepartmentId(Long departmentId) { this.departmentId = departmentId; }
    public Integer getSemester() { return semester; }
    public void setSemester(Integer semester) { this.semester = semester; }
}
