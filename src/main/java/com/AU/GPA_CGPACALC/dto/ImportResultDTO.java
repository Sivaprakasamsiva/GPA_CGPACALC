package com.AU.GPA_CGPACALC.dto;

import java.util.*;

public class ImportResultDTO {
    private int semestersCreated = 0;
    private int subjectsCreated = 0;
    private final Map<String, String> errors = new LinkedHashMap<>();

    public int getSemestersCreated() { return semestersCreated; }
    public void setSemestersCreated(int semestersCreated) { this.semestersCreated = semestersCreated; }
    public void incrementSemestersCreated() { this.semestersCreated++; }

    public int getSubjectsCreated() { return subjectsCreated; }
    public void setSubjectsCreated(int subjectsCreated) { this.subjectsCreated = subjectsCreated; }
    public void incrementSubjectsCreated() { this.subjectsCreated++; }

    public Map<String, String> getErrors() { return errors; }

    public void addError(String key, String message) {
        this.errors.put(key, message);
    }

    public boolean hasErrors() { return !this.errors.isEmpty(); }
}
