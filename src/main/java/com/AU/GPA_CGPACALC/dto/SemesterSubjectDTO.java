package com.AU.GPA_CGPACALC.dto;

public class SemesterSubjectDTO {

    private String courseCode;
    private String courseTitle;
    private Double credits;
    private String grade;
    private Double gradePoint; // e.g. 10.0 for O

    public SemesterSubjectDTO() {
    }

    public SemesterSubjectDTO(String courseCode,
                              String courseTitle,
                              Double credits,
                              String grade,
                              Double gradePoint) {
        this.courseCode = courseCode;
        this.courseTitle = courseTitle;
        this.credits = credits;
        this.grade = grade;
        this.gradePoint = gradePoint;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getCourseTitle() {
        return courseTitle;
    }

    public void setCourseTitle(String courseTitle) {
        this.courseTitle = courseTitle;
    }

    public Double getCredits() {
        return credits;
    }

    public void setCredits(Double credits) {
        this.credits = credits;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public Double getGradePoint() {
        return gradePoint;
    }

    public void setGradePoint(Double gradePoint) {
        this.gradePoint = gradePoint;
    }
}
