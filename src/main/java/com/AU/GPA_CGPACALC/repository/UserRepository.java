package com.AU.GPA_CGPACALC.repository;

import com.AU.GPA_CGPACALC.entity.User;
import com.AU.GPA_CGPACALC.entity.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Boolean existsByEmail(String email);
    long countByRole(Role role);


    // NEW: find users by role with pagination
    Page<User> findByRole(Role role, Pageable pageable);
}
