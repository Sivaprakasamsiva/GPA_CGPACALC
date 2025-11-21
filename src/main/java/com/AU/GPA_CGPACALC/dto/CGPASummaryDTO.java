package com.AU.GPA_CGPACALC.dto;

import java.util.List;

public class CGPASummaryDTO {

    private Double cgpa;
    private Double totalCredits;
    private Double totalPoints;
    private List<CGPASemesterDTO> semesters;

    public CGPASummaryDTO() {
    }

    public CGPASummaryDTO(Double cgpa,
                          Double totalCredits,
                          Double totalPoints,
                          List<CGPASemesterDTO> semesters) {
        this.cgpa = cgpa;
        this.totalCredits = totalCredits;
        this.totalPoints = totalPoints;
        this.semesters = semesters;
    }

    public Double getCgpa() {
        return cgpa;
    }

    public void setCgpa(Double cgpa) {
        this.cgpa = cgpa;
    }

    public Double getTotalCredits() {
        return totalCredits;
    }

    public void setTotalCredits(Double totalCredits) {
        this.totalCredits = totalCredits;
    }

    public Double getTotalPoints() {
        return totalPoints;
    }

    public void setTotalPoints(Double totalPoints) {
        this.totalPoints = totalPoints;
    }

    public List<CGPASemesterDTO> getSemesters() {
        return semesters;
    }

    public void setSemesters(List<CGPASemesterDTO> semesters) {
        this.semesters = semesters;
    }
}
