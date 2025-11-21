package com.AU.GPA_CGPACALC.dto;

import java.util.List;

public class GPARequest {
    private Integer semester;
    private List<GradeEntryRequest> subjects;

    // Constructors
    public GPARequest() {}

    public GPARequest(Integer semester, List<GradeEntryRequest> subjects) {
        this.semester = semester;
        this.subjects = subjects;
    }

    // Getters and Setters
    public Integer getSemester() { return semester; }
    public void setSemester(Integer semester) { this.semester = semester; }

    public List<GradeEntryRequest> getSubjects() { return subjects; }
    public void setSubjects(List<GradeEntryRequest> subjects) { this.subjects = subjects; }
}