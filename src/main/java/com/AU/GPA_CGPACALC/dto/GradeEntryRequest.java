package com.AU.GPA_CGPACALC.dto;

public class GradeEntryRequest {
    private Long subjectId;
    private String grade;

    // Constructors
    public GradeEntryRequest() {}

    public GradeEntryRequest(Long subjectId, String grade) {
        this.subjectId = subjectId;
        this.grade = grade;
    }

    // Getters and Setters
    public Long getSubjectId() { return subjectId; }
    public void setSubjectId(Long subjectId) { this.subjectId = subjectId; }

    public String getGrade() { return grade; }
    public void setGrade(String grade) { this.grade = grade; }
}