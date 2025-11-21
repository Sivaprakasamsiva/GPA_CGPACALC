package com.AU.GPA_CGPACALC.repository;

import com.AU.GPA_CGPACALC.entity.Semester;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface SemesterRepository extends JpaRepository<Semester, Long> {

    List<Semester> findByRegulationIdAndDepartmentIdOrderByNumberAsc(Long regulationId, Long departmentId);

    List<Semester> findByRegulationIdAndDepartmentIdAndActiveTrueOrderByNumberAsc(Long regulationId, Long departmentId);

    Optional<Semester> findByRegulationIdAndDepartmentIdAndNumber(Long regulationId, Long departmentId, Integer number);

    boolean existsByRegulationIdAndDepartmentIdAndNumber(Long regulationId, Long departmentId, Integer number);

    List<Semester> findByDepartmentId(Long departmentId);

    List<Semester> findByDepartmentIdOrderByNumberAsc(Long departmentId);
}
