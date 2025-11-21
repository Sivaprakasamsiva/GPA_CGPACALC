package com.AU.GPA_CGPACALC.repository;

import com.AU.GPA_CGPACALC.entity.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SubjectRepository extends JpaRepository<Subject, Long> {

    // simple lookup used by StudentService and AdminService
    List<Subject> findByRegulationIdAndDepartmentIdAndSemester(Long regulationId, Long departmentId, Integer semester);

    boolean existsByCodeAndRegulationIdAndDepartmentIdAndSemester(String code, Long regulationId, Long departmentId, Integer semester);

    // support delete-by-department in AdminService
    List<Subject> findByDepartmentId(Long departmentId);

    // support findFiltered (used by admin getSubjects)
    @Query("select s from Subject s where "
            + "(:regId is null or s.regulation.id = :regId) and "
            + "(:deptId is null or s.department.id = :deptId) and "
            + "(:semester is null or s.semester = :semester) "
            + "order by s.code")
    List<Subject> findFiltered(@Param("regId") Long regId, @Param("deptId") Long deptId, @Param("semester") Integer semester);

    // convenience for student GPA page – ordered by code
    List<Subject> findByRegulationIdAndDepartmentIdAndSemesterOrderByCodeAsc(Long regulationId, Long departmentId, Integer semester);
}
