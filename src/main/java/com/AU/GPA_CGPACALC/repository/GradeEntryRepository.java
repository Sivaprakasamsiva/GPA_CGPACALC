package com.AU.GPA_CGPACALC.repository;

import com.AU.GPA_CGPACALC.entity.GradeEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface GradeEntryRepository extends JpaRepository<GradeEntry, Long> {

    List<GradeEntry> findByUserAndSemester(com.AU.GPA_CGPACALC.entity.User user, Integer semester);
    List<GradeEntry> findByUser(com.AU.GPA_CGPACALC.entity.User user);
    List<GradeEntry> findByUserIdAndSemesterIn(Long userId, List<Integer> semesters);

    // ⭐ This is the required UPSERT method
    @Modifying
    @Transactional
    @Query(value = """
        INSERT INTO grade_entries (user_id, subject_id, semester, grade, points, created_at)
        VALUES (?1, ?2, ?3, ?4, ?5, NOW())
        ON DUPLICATE KEY UPDATE 
            grade = VALUES(grade),
            points = VALUES(points),
            created_at = NOW()
    """, nativeQuery = true)
    void upsert(Long userId, Long subjectId, Integer semester, String grade, Double points);
}
