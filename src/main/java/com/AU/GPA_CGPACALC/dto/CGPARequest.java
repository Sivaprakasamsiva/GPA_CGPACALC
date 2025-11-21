package com.AU.GPA_CGPACALC.dto;

import java.util.List;

public class CGPARequest {
    private List<Integer> semesterIds;

    // Constructors
    public CGPARequest() {}

    public CGPARequest(List<Integer> semesterIds) {
        this.semesterIds = semesterIds;
    }

    // Getters and Setters
    public List<Integer> getSemesterIds() { return semesterIds; }
    public void setSemesterIds(List<Integer> semesterIds) { this.semesterIds = semesterIds; }
}