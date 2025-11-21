package com.AU.GPA_CGPACALC.controller;

import com.AU.GPA_CGPACALC.dto.*;
import com.AU.GPA_CGPACALC.entity.Regulation;
import com.AU.GPA_CGPACALC.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private UserService userService;

    @Autowired
    private ReportService reportService;

    @Autowired
    private StudentService studentService;

    // -------------------------------------------------------------
    // REGULATIONS
    // -------------------------------------------------------------
    @GetMapping("/regulations")
    public ResponseEntity<List<RegulationResponse>> getRegulations() {
        return ResponseEntity.ok(adminService.getRegulations());
    }

    @PostMapping("/regulations")
    public ResponseEntity<RegulationResponse> createRegulation(@RequestBody Regulation payload) {
        return ResponseEntity.ok(adminService.createRegulation(payload));
    }

    @PutMapping("/regulations/{id}")
    public ResponseEntity<RegulationResponse> updateRegulation(
            @PathVariable Long id, @RequestBody Regulation payload) {
        return ResponseEntity.ok(adminService.updateRegulation(id, payload));
    }

    @DeleteMapping("/regulations/{id}")
    public ResponseEntity<Void> deleteRegulation(@PathVariable Long id) {
        adminService.deleteRegulation(id);
        return ResponseEntity.noContent().build();
    }

    // -------------------------------------------------------------
    // DEPARTMENTS
    // -------------------------------------------------------------
    @GetMapping("/departments")
    public ResponseEntity<List<DepartmentResponse>> getDepartments() {
        return ResponseEntity.ok(adminService.getDepartments());
    }

    @PostMapping("/departments")
    public ResponseEntity<DepartmentResponse> createDepartment(@RequestBody DepartmentRequest req) {
        return ResponseEntity.ok(adminService.createDepartment(req));
    }

    @PutMapping("/departments/{id}")
    public ResponseEntity<DepartmentResponse> updateDepartment(
            @PathVariable Long id, @RequestBody DepartmentRequest req) {
        return ResponseEntity.ok(adminService.updateDepartment(id, req));
    }

    @DeleteMapping("/departments/{id}")
    public ResponseEntity<Void> deleteDepartment(@PathVariable Long id) {
        adminService.deleteDepartment(id);
        return ResponseEntity.noContent().build();
    }

    // -------------------------------------------------------------
    // SEMESTERS CRUD
    // -------------------------------------------------------------
    @GetMapping("/semesters")
    public ResponseEntity<List<SemesterResponse>> getSemesters(
            @RequestParam Long regulationId,
            @RequestParam Long departmentId) {
        return ResponseEntity.ok(adminService.getSemesters(regulationId, departmentId));
    }

    @PostMapping("/semesters")
    public ResponseEntity<SemesterResponse> createSemester(@RequestBody SemesterRequest req) {
        return ResponseEntity.ok(adminService.createSemester(req));
    }

    @PutMapping("/semesters/{id}")
    public ResponseEntity<SemesterResponse> updateSemester(
            @PathVariable Long id, @RequestBody SemesterRequest req) {
        return ResponseEntity.ok(adminService.updateSemester(id, req));
    }

    @DeleteMapping("/semesters/{id}")
    public ResponseEntity<Void> deleteSemester(@PathVariable Long id) {
        adminService.deleteSemester(id);
        return ResponseEntity.noContent().build();
    }

    // -------------------------------------------------------------
    // SUBJECTS CRUD
    // -------------------------------------------------------------
    @GetMapping("/subjects")
    public ResponseEntity<List<SubjectResponse>> getSubjects(
            @RequestParam(required = false) Long regulationId,
            @RequestParam(required = false) Long departmentId,
            @RequestParam(required = false) Integer semester) {

        return ResponseEntity.ok(adminService.getSubjects(regulationId, departmentId, semester));
    }

    @PostMapping("/subjects")
    public ResponseEntity<SubjectResponse> addSubject(@RequestBody SubjectRequest req) {
        return ResponseEntity.ok(adminService.addSubject(req));
    }

    @PutMapping("/subjects/{id}")
    public ResponseEntity<SubjectResponse> updateSubject(
            @PathVariable Long id, @RequestBody SubjectRequest req) {
        return ResponseEntity.ok(adminService.updateSubject(id, req));
    }

    @DeleteMapping("/subjects/{id}")
    public ResponseEntity<Void> deleteSubject(@PathVariable Long id) {
        adminService.deleteSubject(id);
        return ResponseEntity.noContent().build();
    }

    // -------------------------------------------------------------
    // STUDENTS
    // -------------------------------------------------------------
    @GetMapping("/students")
    public ResponseEntity<List<UserResponse>> getStudents() {
        return ResponseEntity.ok(userService.getAllStudents());
    }

    @PostMapping("/create-student")
    public ResponseEntity<UserResponse> createStudent(@RequestBody CreateStudentRequest req) {
        return ResponseEntity.ok(userService.createStudent(req));
    }

    // -------------------------------------------------------------
    // CSV IMPORT
    // -------------------------------------------------------------
    @PostMapping(value = "/import/csv", consumes = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<ImportResultDTO> importCsv(@RequestBody String csvText) {
        ImportResultDTO result = adminService.importFromCsv(csvText);
        return ResponseEntity.ok(result);
    }

    // -------------------------------------------------------------
    // BULK IMPORT SEMESTERS (ONLY ONE VERSION)
    // -------------------------------------------------------------
    @PostMapping("/import/semesters")
    public ResponseEntity<?> bulkImportSemesters(@RequestBody List<BulkSemesterDTO> list) {
        try {
            adminService.bulkImportSemesters(list);
            return ResponseEntity.ok("Semesters imported successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    // -------------------------------------------------------------
    // BULK IMPORT SUBJECTS
    // -------------------------------------------------------------
    @PostMapping("/import/subjects")
    public ResponseEntity<?> importSubjects(@RequestBody List<SubjectImportDTO> list) {
        try {
            BulkImportRequest req = new BulkImportRequest(null, list);
            ImportResultDTO result = adminService.importBulk(req);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    // -------------------------------------------------------------
    // REPORTS
    // -------------------------------------------------------------
    @GetMapping("/reports")
    public ResponseEntity<List<ReportResponse>> getReports() {
        return ResponseEntity.ok(reportService.getAllReports());
    }

    @GetMapping("/reports/{id}/download")
    public ResponseEntity<byte[]> downloadReport(@PathVariable Long id) {
        byte[] pdf = reportService.downloadReport(id);

        HttpHeaders h = new HttpHeaders();
        h.setContentType(MediaType.APPLICATION_PDF);
        h.setContentDispositionFormData("attachment", "report-" + id + ".pdf");

        return new ResponseEntity<>(pdf, h, HttpStatus.OK);
    }

    // -------------------------------------------------------------
    // DASHBOARD STATS
    // -------------------------------------------------------------
    @GetMapping("/stats/students")
    public ResponseEntity<CountResponse> countStudents() {
        return ResponseEntity.ok(new CountResponse(userService.countStudents()));
    }

    @GetMapping("/stats/departments")
    public ResponseEntity<CountResponse> countDepartments() {
        return ResponseEntity.ok(new CountResponse(adminService.countDepartments()));
    }

    @GetMapping("/stats/regulations")
    public ResponseEntity<CountResponse> countRegulations() {
        return ResponseEntity.ok(new CountResponse(adminService.countRegulations()));
    }

    @GetMapping("/stats/reports")
    public ResponseEntity<CountResponse> countReports() {
        return ResponseEntity.ok(new CountResponse(reportService.countReports()));
    }

    // -------------------------------------------------------------
    // STUDENT HISTORY
    // -------------------------------------------------------------
    @GetMapping("/student-history/{id}")
    public ResponseEntity<?> getStudentHistoryByAdmin(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(studentService.getHistoryByAdmin(id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("ERROR: " + e.getMessage());
        }
    }

    // -------------------------------------------------------------
    // MARKSHEET BY SEMESTER
    // -------------------------------------------------------------
    @GetMapping("/marksheet/{studentId}/{semester}")
    public ResponseEntity<?> getMarksheetByAdmin(
            @PathVariable Long studentId,
            @PathVariable Integer semester) {
        try {
            byte[] pdf = studentService.generateMarksheetByAdmin(studentId, semester);

            HttpHeaders h = new HttpHeaders();
            h.setContentType(MediaType.APPLICATION_PDF);
            h.setContentDispositionFormData(
                    "attachment",
                    "student-" + studentId + "-sem-" + semester + ".pdf"
            );

            return new ResponseEntity<>(pdf, h, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("ERROR: " + e.getMessage());
        }
    }

    // -------------------------------------------------------------
    // SEMESTER REPORT
    // -------------------------------------------------------------
    @GetMapping("/semester-report/{studentId}/{semester}")
    public ResponseEntity<?> getSemesterReportByAdmin(
            @PathVariable Long studentId,
            @PathVariable Integer semester) {
        try {
            SemesterReportDTO dto = studentService.getSemesterReportByAdmin(studentId, semester);
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("ERROR: " + e.getMessage());
        }
    }
}
