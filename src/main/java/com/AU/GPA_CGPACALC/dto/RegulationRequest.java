package com.AU.GPA_CGPACALC.dto;

public class RegulationRequest {
    private String name;
    private Integer year;
    private String description;

    public RegulationRequest() {}

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Integer getYear() { return year; }
    public void setYear(Integer year) { this.year = year; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
