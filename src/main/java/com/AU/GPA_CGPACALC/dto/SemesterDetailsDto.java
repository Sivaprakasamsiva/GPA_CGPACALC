package com.AU.GPA_CGPACALC.dto;

public class SemesterDetailsDto {
    private Integer mandatoryCount;
    private Integer electiveCount;

    public SemesterDetailsDto() {}

    public SemesterDetailsDto(Integer mandatoryCount, Integer electiveCount) {
        this.mandatoryCount = mandatoryCount;
        this.electiveCount = electiveCount;
    }

    public Integer getMandatoryCount() {
        return mandatoryCount;
    }

    public void setMandatoryCount(Integer mandatoryCount) {
        this.mandatoryCount = mandatoryCount;
    }

    public Integer getElectiveCount() {
        return electiveCount;
    }

    public void setElectiveCount(Integer electiveCount) {
        this.electiveCount = electiveCount;
    }
}
