package com.AU.GPA_CGPACALC.dto;

public class SubjectGradeDTO {

    private Long subjectId;
    private String code;
    private String name;
    private Double credits;
    private String grade;
    private Double points;

    public SubjectGradeDTO(Long subjectId, String code, String name, Double credits, String grade, Double points) {
        this.subjectId = subjectId;
        this.code = code;
        this.name = name;
        this.credits = credits;
        this.grade = grade;
        this.points = points;
    }

    public Long getSubjectId() { return subjectId; }
    public String getCode() { return code; }
    public String getName() { return name; }
    public Double getCredits() { return credits; }
    public String getGrade() { return grade; }
    public Double getPoints() { return points; }
}
