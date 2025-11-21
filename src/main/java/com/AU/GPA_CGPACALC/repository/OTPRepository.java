package com.AU.GPA_CGPACALC.repository;

import com.AU.GPA_CGPACALC.entity.OTP;
import com.AU.GPA_CGPACALC.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface OTPRepository extends JpaRepository<OTP, Long> {
    Optional<OTP> findByUserAndCodeAndUsedFalseAndExpiresAtAfter(User user, String code, LocalDateTime now);
    Optional<OTP> findByUserAndUsedFalseAndExpiresAtAfter(User user, LocalDateTime now);
}