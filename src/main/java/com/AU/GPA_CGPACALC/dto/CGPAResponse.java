package com.AU.GPA_CGPACALC.dto;

public class CGPAResponse {
    private Double cgpa;
    private Double totalCredits;
    private Double totalPoints;

    // Constructors
    public CGPAResponse() {}

    public CGPAResponse(Double cgpa, Double totalCredits, Double totalPoints) {
        this.cgpa = cgpa;
        this.totalCredits = totalCredits;
        this.totalPoints = totalPoints;
    }

    // Getters and Setters
    public Double getCgpa() { return cgpa; }
    public void setCgpa(Double cgpa) { this.cgpa = cgpa; }

    public Double getTotalCredits() { return totalCredits; }
    public void setTotalCredits(Double totalCredits) { this.totalCredits = totalCredits; }

    public Double getTotalPoints() { return totalPoints; }
    public void setTotalPoints(Double totalPoints) { this.totalPoints = totalPoints; }
}