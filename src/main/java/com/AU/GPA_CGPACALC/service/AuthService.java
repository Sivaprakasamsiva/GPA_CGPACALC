package com.AU.GPA_CGPACALC.service;

import com.AU.GPA_CGPACALC.dto.*;
import com.AU.GPA_CGPACALC.entity.*;
import com.AU.GPA_CGPACALC.repository.*;
import com.AU.GPA_CGPACALC.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuthService {

    @Autowired private UserRepository userRepository;
    @Autowired private RegulationRepository regulationRepository;
    @Autowired private DepartmentRepository departmentRepository;

    // OTP components kept, but no OTP logic used anymore
    @Autowired private OTPRepository otpRepository;
    @Autowired private EmailService emailService;

    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private AuthenticationManager authenticationManager;
    @Autowired private JwtUtil jwtUtil;

    // ---------------------- REGISTER (NO OTP) ----------------------
    public User register(RegisterRequest req) {

        if (userRepository.existsByEmail(req.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        Regulation regulation = regulationRepository.findById(req.getRegulationId())
                .orElseThrow(() -> new RuntimeException("Regulation not found"));

        Department department = departmentRepository.findById(req.getDepartmentId())
                .orElseThrow(() -> new RuntimeException("Department not found"));

        User user = new User();
        user.setName(req.getName());
        user.setEmail(req.getEmail());
        user.setPassword(passwordEncoder.encode(req.getPassword()));
        user.setRegulation(regulation);
        user.setDepartment(department);
        user.setRole(Role.STUDENT);

        // AUTO VERIFY USER
        user.setVerified(true);

        User saved = userRepository.save(user);
        return saved;
    }

    // ---------------------- LOGIN ----------------------
    public AuthResponse login(AuthRequest req) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.getEmail(), req.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        User user = userRepository.findByEmail(req.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!user.isVerified()) {
            throw new RuntimeException("Account not verified.");
        }

        user.setLastLogin(LocalDateTime.now());
        userRepository.save(user);

        org.springframework.security.core.userdetails.User jwtUser =
                new org.springframework.security.core.userdetails.User(
                        user.getEmail(),
                        user.getPassword(),
                        java.util.Collections.singletonList(
                                new SimpleGrantedAuthority(user.getRole().name())
                        )
                );

        String token = jwtUtil.generateToken(jwtUser);

        UserResponse userResponse = convertToUserResponse(user);

        return new AuthResponse(token, userResponse);
    }

    // ---------------------- UNUSED (OTP DISABLED) ----------------------
    public void verifyOtp(String email, String otpCode) {
        throw new RuntimeException("OTP system disabled. Direct login after registration.");
    }

    public void resendOtp(String email) {
        throw new RuntimeException("OTP system disabled.");
    }

    // ---------------------- USER → USERRESPONSE ----------------------
    private UserResponse convertToUserResponse(User user) {

        UserResponse ur = new UserResponse();
        ur.setId(user.getId());
        ur.setName(user.getName());
        ur.setEmail(user.getEmail());
        ur.setRole(user.getRole().toString());

        if (user.getRegulation() != null) {
            RegulationResponse rr = new RegulationResponse();
            rr.setId(user.getRegulation().getId());
            rr.setName(user.getRegulation().getName());
            rr.setYear(user.getRegulation().getYear());
            ur.setRegulation(rr);
        }

        if (user.getDepartment() != null) {
            DepartmentResponse dr = new DepartmentResponse();
            dr.setId(user.getDepartment().getId());
            dr.setName(user.getDepartment().getName());
            dr.setCode(user.getDepartment().getCode());
            ur.setDepartment(dr);
        }

        return ur;
    }
}
