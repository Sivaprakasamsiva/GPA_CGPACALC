package com.AU.GPA_CGPACALC.dto;

import java.util.List;

public class BulkInsertResultDTO {
    private int insertedSemesters;
    private int insertedSubjects;
    private int skippedSubjects; // duplicates
    private List<String> errors;

    public BulkInsertResultDTO() {}

    public BulkInsertResultDTO(int insertedSemesters, int insertedSubjects, int skippedSubjects, List<String> errors) {
        this.insertedSemesters = insertedSemesters;
        this.insertedSubjects = insertedSubjects;
        this.skippedSubjects = skippedSubjects;
        this.errors = errors;
    }

    public int getInsertedSemesters() { return insertedSemesters; }
    public void setInsertedSemesters(int insertedSemesters) { this.insertedSemesters = insertedSemesters; }

    public int getInsertedSubjects() { return insertedSubjects; }
    public void setInsertedSubjects(int insertedSubjects) { this.insertedSubjects = insertedSubjects; }

    public int getSkippedSubjects() { return skippedSubjects; }
    public void setSkippedSubjects(int skippedSubjects) { this.skippedSubjects = skippedSubjects; }

    public List<String> getErrors() { return errors; }
    public void setErrors(List<String> errors) { this.errors = errors; }
}
