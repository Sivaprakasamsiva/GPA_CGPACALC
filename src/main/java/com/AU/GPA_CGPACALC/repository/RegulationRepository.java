package com.AU.GPA_CGPACALC.repository;

import com.AU.GPA_CGPACALC.entity.Regulation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RegulationRepository extends JpaRepository<Regulation, Long> {
    Optional<Regulation> findByName(String name);
}