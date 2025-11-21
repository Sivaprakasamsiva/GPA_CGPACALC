package com.AU.GPA_CGPACALC.dto;

public class RegulationResponse {
    private Long id;
    private String name;
    private Integer year;
    private String description;

    public RegulationResponse() {}

    // Constructor used by services: (id, name, year)
    public RegulationResponse(Long id, String name, Integer year) {
        this.id = id;
        this.name = name;
        this.year = year;
    }

    // Full constructor (id, name, year, description)
    public RegulationResponse(Long id, String name, Integer year, String description) {
        this.id = id;
        this.name = name;
        this.year = year;
        this.description = description;
    }

    // getters / setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Integer getYear() { return year; }
    public void setYear(Integer year) { this.year = year; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
