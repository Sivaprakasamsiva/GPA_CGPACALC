package com.AU.GPA_CGPACALC.dto;

import java.util.List;

public class SemesterReportDTO {

    private Integer semester;
    private Double gpa;
    private Double totalCredits;
    private Double totalPoints;
    private List<SemesterSubjectDTO> subjects;

    public SemesterReportDTO() {
    }

    public SemesterReportDTO(Integer semester,
                             Double gpa,
                             Double totalCredits,
                             Double totalPoints,
                             List<SemesterSubjectDTO> subjects) {
        this.semester = semester;
        this.gpa = gpa;
        this.totalCredits = totalCredits;
        this.totalPoints = totalPoints;
        this.subjects = subjects;
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

    public List<SemesterSubjectDTO> getSubjects() {
        return subjects;
    }

    public void setSubjects(List<SemesterSubjectDTO> subjects) {
        this.subjects = subjects;
    }
}
