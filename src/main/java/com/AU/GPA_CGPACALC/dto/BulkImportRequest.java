package com.AU.GPA_CGPACALC.dto;

import java.util.List;

public class BulkImportRequest {

    private List<SemesterImportDTO> semesters;
    private List<SubjectImportDTO> subjects;

    public BulkImportRequest() {}

    public BulkImportRequest(List<SemesterImportDTO> semesters, List<SubjectImportDTO> subjects) {
        this.semesters = semesters;
        this.subjects = subjects;
    }

    public List<SemesterImportDTO> getSemesters() {
        return semesters;
    }

    public void setSemesters(List<SemesterImportDTO> semesters) {
        this.semesters = semesters;
    }

    public List<SubjectImportDTO> getSubjects() {
        return subjects;
    }

    public void setSubjects(List<SubjectImportDTO> subjects) {
        this.subjects = subjects;
    }
}
