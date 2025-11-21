package com.AU.GPA_CGPACALC.controller;

import com.AU.GPA_CGPACALC.dto.*;
import com.AU.GPA_CGPACALC.entity.Department;
import com.AU.GPA_CGPACALC.entity.Regulation;
import com.AU.GPA_CGPACALC.repository.DepartmentRepository;
import com.AU.GPA_CGPACALC.repository.RegulationRepository;
import com.AU.GPA_CGPACALC.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private RegulationRepository regulationRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    // ============================
    // REGISTER
    // ============================
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest registerRequest) {
        try {
            authService.register(registerRequest);
            return ResponseEntity.ok().body("Registration successful.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // ============================
    // LOGIN
    // ============================
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody AuthRequest authRequest) {
        try {
            AuthResponse authResponse = authService.login(authRequest);
            return ResponseEntity.ok(authResponse);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // ============================
    // PUBLIC SAFE — REGULATIONS
    // ============================
    @GetMapping("/public-regulations")
    public ResponseEntity<?> safePublicRegulations() {
        List<PublicRegulationDTO> list = regulationRepository.findAll()
                .stream()
                .map(r -> new PublicRegulationDTO(
                        r.getId(),
                        r.getName(),
                        r.getYear()
                ))
                .toList();

        return ResponseEntity.ok(list);
    }

    // ============================
    // PUBLIC SAFE — DEPARTMENTS
    // ============================
    @GetMapping("/public-departments")
    public ResponseEntity<?> safePublicDepartments() {
        List<PublicDepartmentDTO> list = departmentRepository.findAll()
                .stream()
                .map(d -> new PublicDepartmentDTO(
                        d.getId(),
                        d.getName(),
                        d.getCode()
                ))
                .toList();

        return ResponseEntity.ok(list);
    }

    // ============================
    // (Optional − kept for future)
    // ============================
    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@Valid @RequestBody OTPRequest otpRequest) {
        try {
            authService.verifyOtp(otpRequest.getEmail(), otpRequest.getOtp());
            return ResponseEntity.ok().body("Email verified successfully.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/resend-otp")
    public ResponseEntity<?> resendOtp(@RequestBody OTPRequest otpRequest) {
        try {
            authService.resendOtp(otpRequest.getEmail());
            return ResponseEntity.ok().body("OTP resent successfully.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
