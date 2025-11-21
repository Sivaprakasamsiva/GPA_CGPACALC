package com.AU.GPA_CGPACALC.service;

import com.AU.GPA_CGPACALC.dto.CreateStudentRequest;
import com.AU.GPA_CGPACALC.dto.UserResponse;
import com.AU.GPA_CGPACALC.dto.RegulationResponse;
import com.AU.GPA_CGPACALC.dto.DepartmentResponse;
import com.AU.GPA_CGPACALC.entity.User;
import com.AU.GPA_CGPACALC.entity.Regulation;
import com.AU.GPA_CGPACALC.entity.Department;
import com.AU.GPA_CGPACALC.repository.UserRepository;
import com.AU.GPA_CGPACALC.repository.RegulationRepository;
import com.AU.GPA_CGPACALC.repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired private UserRepository userRepository;
    @Autowired private RegulationRepository regulationRepository;
    @Autowired private DepartmentRepository departmentRepository;
    @Autowired private PasswordEncoder passwordEncoder;

    // return all students (role == STUDENT)
    public List<UserResponse> getAllStudents() {
        List<User> users = userRepository.findAll();
        // filter role STUDENT so admins / others are excluded if your repo returns all users
        return users.stream()
                .filter(u -> u.getRole() != null && u.getRole().toString().equalsIgnoreCase("STUDENT"))
                .map(this::toUserResponse)
                .collect(Collectors.toList());
    }

    public long countStudents() {
        // if repository has a specialized count method, you could call that.
        return userRepository.findAll().stream().filter(u -> u.getRole() != null && u.getRole().toString().equalsIgnoreCase("STUDENT")).count();
    }

    public UserResponse createStudent(CreateStudentRequest req) {
        if (req.getEmail() == null || req.getEmail().isBlank()) {
            throw new RuntimeException("Email required");
        }
        if (userRepository.findByEmail(req.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        User user = new User();
        user.setName(req.getName());
        user.setEmail(req.getEmail());
        user.setPassword(passwordEncoder.encode(req.getPassword()));

        // 🔥 IMPORTANT FIX: admin-created students should be active/verified
        user.setVerified(true);

        if (req.getRegulationId() != null) {
            Regulation reg = regulationRepository.findById(req.getRegulationId())
                    .orElseThrow(() -> new RuntimeException("Regulation not found"));
            user.setRegulation(reg);
        }

        if (req.getDepartmentId() != null) {
            Department dep = departmentRepository.findById(req.getDepartmentId())
                    .orElseThrow(() -> new RuntimeException("Department not found"));
            user.setDepartment(dep);
        }

        userRepository.save(user);
        return toUserResponse(user);
    }


    private UserResponse toUserResponse(User u) {
        UserResponse res = new UserResponse();
        res.setId(u.getId());
        res.setName(u.getName());
        res.setEmail(u.getEmail());
        res.setRole(u.getRole() != null ? u.getRole().toString() : null);

        if (u.getRegulation() != null) {
            Regulation r = u.getRegulation();
            res.setRegulation(new RegulationResponse(r.getId(), r.getName(), r.getYear(), r.getDescription()));
        }

        if (u.getDepartment() != null) {
            Department d = u.getDepartment();
            Long regId = d.getRegulation() != null ? d.getRegulation().getId() : null;
            res.setDepartment(new DepartmentResponse(d.getId(), d.getName(), d.getCode(), d.getDescription(), d.getSemesterCount(), regId));
        }

        return res;
    }
}
