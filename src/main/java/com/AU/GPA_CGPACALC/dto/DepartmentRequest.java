package com.AU.GPA_CGPACALC.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class DepartmentRequest {
    private Long id;

    @NotBlank
    @Size(max = 100)
    private String name;

    @NotBlank
    @Size(max = 20)
    private String code;

    @Size(max = 500)
    private String description;

    // department belongs to a regulation
    private Long regulationId;

    // semester count (allow null)
    private Integer semesterCount;

    public DepartmentRequest() {}

    // getters / setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Long getRegulationId() { return regulationId; }
    public void setRegulationId(Long regulationId) { this.regulationId = regulationId; }

    public Integer getSemesterCount() { return semesterCount; }
    public void setSemesterCount(Integer semesterCount) { this.semesterCount = semesterCount; }
}
