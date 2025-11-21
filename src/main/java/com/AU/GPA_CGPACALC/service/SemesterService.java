package com.AU.GPA_CGPACALC.service;

import com.AU.GPA_CGPACALC.entity.Semester;
import com.AU.GPA_CGPACALC.repository.SemesterRepository;
import org.springframework.stereotype.Service;

@Service
public class SemesterService {

    private final SemesterRepository semesterRepository;

    public SemesterService(SemesterRepository semesterRepository) {
        this.semesterRepository = semesterRepository;
    }

    public Semester getSemester(Long regulationId, Long departmentId, Integer number) {
        return semesterRepository
                .findByRegulationIdAndDepartmentIdAndNumber(regulationId, departmentId, number)
                .orElseThrow(() -> new RuntimeException(
                        "Semester not found for Regulation=" + regulationId +
                                ", Department=" + departmentId +
                                ", Semester=" + number
                ));
    }
}
