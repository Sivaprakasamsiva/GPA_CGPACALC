package com.AU.GPA_CGPACALC.dto;

public class DepartmentResponse {

    private Long id;
    private String name;
    private String code;
    private String description;
    private Integer semesterCount;
    private Long regulationId;

    public DepartmentResponse() {}

    // 5-arg constructor (your departmentService uses this)
    public DepartmentResponse(Long id, String name, String code, String description, Integer semesterCount) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.description = description;
        this.semesterCount = semesterCount;
    }

    // 6-arg constructor (AdminService uses this)
    public DepartmentResponse(Long id, String name, String code, String description, Integer semesterCount, Long regulationId) {
        this(id, name, code, description, semesterCount);
        this.regulationId = regulationId;
    }

    // also alternate order (your StudentService used)
    public DepartmentResponse(Long id, String name, String code, String description, Long regulationId, Integer semesterCount) {
        this(id, name, code, description, semesterCount);
        this.regulationId = regulationId;
    }

    // getters/setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Integer getSemesterCount() { return semesterCount; }
    public void setSemesterCount(Integer semesterCount) { this.semesterCount = semesterCount; }

    public Long getRegulationId() { return regulationId; }
    public void setRegulationId(Long regulationId) { this.regulationId = regulationId; }
}
