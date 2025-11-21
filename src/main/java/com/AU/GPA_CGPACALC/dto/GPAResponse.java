package com.AU.GPA_CGPACALC.dto;

public class GPAResponse {
    private Double gpa;
    private Double totalCredits;
    private Double totalPoints;

    // Constructors
    public GPAResponse() {}

    public GPAResponse(Double gpa, Double totalCredits, Double totalPoints) {
        this.gpa = gpa;
        this.totalCredits = totalCredits;
        this.totalPoints = totalPoints;
    }

    // Getters and Setters
    public Double getGpa() { return gpa; }
    public void setGpa(Double gpa) { this.gpa = gpa; }

    public Double getTotalCredits() { return totalCredits; }
    public void setTotalCredits(Double totalCredits) { this.totalCredits = totalCredits; }

    public Double getTotalPoints() { return totalPoints; }
    public void setTotalPoints(Double totalPoints) { this.totalPoints = totalPoints; }
}