package com.AU.GPA_CGPACALC.repository;

import com.AU.GPA_CGPACALC.entity.GpaResult;
import com.AU.GPA_CGPACALC.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GpaResultRepository extends JpaRepository<GpaResult, Long> {
    List<GpaResult> findByUser(User user);
    Optional<GpaResult> findByUserAndSemester(User user, Integer semester);
    List<GpaResult> findByUserIdAndSemesterIn(Long userId, List<Integer> semesters);
}