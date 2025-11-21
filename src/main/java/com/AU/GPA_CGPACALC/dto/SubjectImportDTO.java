package com.AU.GPA_CGPACALC.dto;

public class SubjectImportDTO {
    private String code;
    private String name;
    private Double credits;
    private Boolean isElective;
    private String type;
    private Long regulationId;
    private Long departmentId;
    private Integer semester;

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Double getCredits() { return credits; }
    public void setCredits(Double credits) { this.credits = credits; }

    public Boolean getIsElective() { return isElective; }
    public void setIsElective(Boolean isElective) { this.isElective = isElective; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public Long getRegulationId() { return regulationId; }
    public void setRegulationId(Long regulationId) { this.regulationId = regulationId; }

    public Long getDepartmentId() { return departmentId; }
    public void setDepartmentId(Long departmentId) { this.departmentId = departmentId; }

    public Integer getSemester() { return semester; }
    public void setSemester(Integer semester) { this.semester = semester; }
}
