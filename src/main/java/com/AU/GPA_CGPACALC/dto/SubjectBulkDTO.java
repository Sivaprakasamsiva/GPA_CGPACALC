package com.AU.GPA_CGPACALC.dto;

public class SubjectBulkDTO {
    private String code;
    private String name;
    private Double credits;
    private Integer semester;
    private String type; // CORE, LAB, ELECTIVE etc.
    private Boolean isElective;

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Double getCredits() { return credits; }
    public void setCredits(Double credits) { this.credits = credits; }

    public Integer getSemester() { return semester; }
    public void setSemester(Integer semester) { this.semester = semester; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public Boolean getIsElective() { return isElective; }
    public void setIsElective(Boolean isElective) { this.isElective = isElective; }
}
