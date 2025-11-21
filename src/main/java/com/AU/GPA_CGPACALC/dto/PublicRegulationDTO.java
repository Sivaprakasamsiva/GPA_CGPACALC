package com.AU.GPA_CGPACALC.dto;

public class PublicRegulationDTO {
    private Long id;
    private String name;
    private Integer year;

    public PublicRegulationDTO(Long id, String name, Integer year) {
        this.id = id;
        this.name = name;
        this.year = year;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public Integer getYear() { return year; }
}
