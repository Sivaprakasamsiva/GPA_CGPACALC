package com.AU.GPA_CGPACALC.controller;

import com.AU.GPA_CGPACALC.dto.*;
import com.AU.GPA_CGPACALC.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/student")
@CrossOrigin(origins = "*")
public class StudentController {

    @Autowired
    private StudentService studentService;

    // ==============================
    // PROFILE
    // ==============================
    @GetMapping("/profile")
    public ResponseEntity<?> getProfile(@AuthenticationPrincipal UserDetails currentUser) {
        try {
            UserResponse res = studentService.getProfile(currentUser.getUsername());
            return ResponseEntity.ok(res);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("ERROR: " + e.getMessage());
        }
    }

    // ==============================
    // SUBJECTS
    // ==============================
    @GetMapping("/subjects")
    public ResponseEntity<?> getSubjects(
            @RequestParam Long regulationId,
            @RequestParam Long departmentId,
            @RequestParam Integer semester
    ) {
        try {
            List<SubjectResponse> subjects =
                    studentService.getSubjects(regulationId, departmentId, semester);
            return ResponseEntity.ok(subjects);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("ERROR: " + e.getMessage());
        }
    }

    // ==============================
    // SEMESTER DETAILS
    // ==============================
    @GetMapping("/semester-details")
    public ResponseEntity<?> getSemesterDetails(
            @RequestParam Long regulationId,
            @RequestParam Long departmentId,
            @RequestParam Integer semester
    ) {
        try {
            SemesterResponse res =
                    studentService.getSemesterDetails(regulationId, departmentId, semester);
            return ResponseEntity.ok(res);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("ERROR: " + e.getMessage());
        }
    }

    // ==============================
    // GPA CALCULATION
    // ==============================
    @PostMapping("/calculate-gpa")
    public ResponseEntity<?> calculateGPA(
            @AuthenticationPrincipal UserDetails currentUser,
            @RequestBody GPARequest request
    ) {
        try {
            GPAResponse res = studentService.calculateGPA(currentUser.getUsername(), request);
            return ResponseEntity.ok(res);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("ERROR: " + e.getMessage());
        }
    }

    @PostMapping("/grades")
    public ResponseEntity<?> saveGrades(
            @AuthenticationPrincipal UserDetails currentUser,
            @RequestBody GPARequest request
    ) {
        try {
            GPAResponse res = studentService.calculateGPA(currentUser.getUsername(), request);
            return ResponseEntity.ok(res);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("ERROR: " + e.getMessage());
        }
    }

    // ==============================
    // CGPA CALCULATION
    // ==============================
    @PostMapping("/calculate-cgpa")
    public ResponseEntity<?> calculateCGPA(
            @AuthenticationPrincipal UserDetails currentUser,
            @RequestBody CGPARequest request
    ) {
        try {
            CGPAResponse res = studentService.calculateCGPA(currentUser.getUsername(), request);
            return ResponseEntity.ok(res);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("ERROR: " + e.getMessage());
        }
    }

    // ==============================
    // HISTORY
    // ==============================
    @GetMapping("/history")
    public ResponseEntity<?> getHistory(
            @AuthenticationPrincipal UserDetails currentUser) {
        try {
            return ResponseEntity.ok(
                    studentService.getHistory(currentUser.getUsername())
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("ERROR: " + e.getMessage());
        }
    }

    // ==============================
    // COMPLETED SEMESTERS LIST
    // ==============================
    @GetMapping("/completed-semesters")
    public ResponseEntity<?> getCompletedSemesters(
            @AuthenticationPrincipal UserDetails currentUser) {
        try {
            return ResponseEntity.ok(
                    studentService.getCompletedSemesters(currentUser.getUsername())
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("ERROR: " + e.getMessage());
        }
    }

    // ==============================
    // SEMESTER REPORT (JSON for frontend PDF)
    // ==============================
    @GetMapping("/semester-report")
    public ResponseEntity<?> getSemesterReport(
            @AuthenticationPrincipal UserDetails currentUser,
            @RequestParam Integer semester
    ) {
        try {
            SemesterReportDTO dto =
                    studentService.getSemesterReport(currentUser.getUsername(), semester);
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("ERROR: " + e.getMessage());
        }
    }

    // ==============================
    // CGPA REPORT (JSON for frontend PDF)
    // ==============================
    @PostMapping("/cgpa-report")
    public ResponseEntity<?> getCgpaReport(
            @AuthenticationPrincipal UserDetails currentUser,
            @RequestBody CGPARequest request
    ) {
        try {
            CGPASummaryDTO dto =
                    studentService.getCGPASummaryData(currentUser.getUsername(), request.getSemesterIds());
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("ERROR: " + e.getMessage());
        }
    }

    // ==============================
    // MARKSHEET PDF (BACKEND PDF)
    // ==============================
    @GetMapping("/marksheet/{semester}")
    public ResponseEntity<byte[]> marksheet(
            @AuthenticationPrincipal UserDetails currentUser,
            @PathVariable Integer semester
    ) {
        try {
            byte[] pdf = studentService.generateMarksheet(currentUser.getUsername(), semester);
            HttpHeaders h = new HttpHeaders();
            h.setContentType(MediaType.APPLICATION_PDF);
            h.setContentDispositionFormData("attachment", "marksheet-sem-" + semester + ".pdf");
            return new ResponseEntity<>(pdf, h, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // ==============================
    // CGPA SUMMARY PDF (BACKEND PDF)
    // ==============================
    @PostMapping("/cgpa-summary")
    public ResponseEntity<byte[]> cgpaSummary(
            @AuthenticationPrincipal UserDetails currentUser,
            @RequestBody CGPARequest request
    ) {
        try {
            byte[] pdf = studentService.generateCGPASummary(currentUser.getUsername(), request.getSemesterIds());
            HttpHeaders h = new HttpHeaders();
            h.setContentType(MediaType.APPLICATION_PDF);
            h.setContentDispositionFormData("attachment", "cgpa-summary.pdf");
            return new ResponseEntity<>(pdf, h, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
