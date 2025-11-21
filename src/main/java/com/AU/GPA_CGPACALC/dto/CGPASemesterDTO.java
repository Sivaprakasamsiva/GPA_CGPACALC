package com.AU.GPA_CGPACALC.dto;

public class CGPASemesterDTO {

    private Integer semester;
    private Double gpa;
    private Double totalCredits;
    private Double totalPoints;

    public CGPASemesterDTO() {
    }

    public CGPASemesterDTO(Integer semester,
                           Double gpa,
                           Double totalCredits,
                           Double totalPoints) {
        this.semester = semester;
        this.gpa = gpa;
        this.totalCredits = totalCredits;
        this.totalPoints = totalPoints;
    }

    public Integer getSemester() {
        return semester;
    }

    public void setSemester(Integer semester) {
        this.semester = semester;
    }

    public Double getGpa() {
        return gpa;
    }

    public void setGpa(Double gpa) {
        this.gpa = gpa;
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
}
