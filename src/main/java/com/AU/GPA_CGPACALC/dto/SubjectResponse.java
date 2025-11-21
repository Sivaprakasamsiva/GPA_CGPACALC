package com.AU.GPA_CGPACALC.dto;

public class SubjectResponse {
    private Long id;
    private String code;
    private String name;
    private Double credits;
    private Boolean isElective;
    private String type;
    private Integer semester;

    public SubjectResponse() {}

    public SubjectResponse(Long id, String code, String name, Double credits, Boolean isElective, String type, Integer semester) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.credits = credits;
        this.isElective = isElective;
        this.type = type;
        this.semester = semester;
    }

    // getters/setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
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
    public Integer getSemester() { return semester; }
    public void setSemester(Integer semester) { this.semester = semester; }
}
