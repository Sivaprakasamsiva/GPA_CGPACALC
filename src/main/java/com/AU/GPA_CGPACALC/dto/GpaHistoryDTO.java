package com.AU.GPA_CGPACALC.dto;

public class GpaHistoryDTO {
    private Integer semester;
    private Double gpa;

    public GpaHistoryDTO(Integer semester, Double gpa) {
        this.semester = semester;
        this.gpa = gpa;
    }

    public Integer getSemester() {
        return semester;
    }

    public Double getGpa() {
        return gpa;
    }
}
