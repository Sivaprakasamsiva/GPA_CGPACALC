package com.AU.GPA_CGPACALC.service;

import com.AU.GPA_CGPACALC.dto.*;
import com.AU.GPA_CGPACALC.entity.*;
import com.AU.GPA_CGPACALC.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * StudentService
 *
 * Full service file — preserves every existing method and behaviour you had.
 * Additions:
 *  - Injected ReportService to persist generated PDFs when backend generates them.
 *  - generateMarksheet, generateCGPASummary, generateMarksheetByAdmin now save the produced PDF bytes
 *    into the reports table by calling reportService.saveReport(...).
 *
 * Nothing else removed or reduced — methods, DTO usage, and mapping are preserved.
 */

@Service
public class StudentService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private SemesterRepository semesterRepository;

    @Autowired
    private GradeEntryRepository gradeEntryRepository;

    @Autowired
    private GpaResultRepository gpaResultRepository;

    @Autowired
    private PDFService pdfService;

    // NEW: report persistence service (saves generated PDF bytes into reports table)
    @Autowired
    private ReportService reportService;

    // Grade to points mapping
    private final Map<String, Double> gradePoints = Map.ofEntries(
            Map.entry("O", 10.0),
            Map.entry("A+", 9.0),
            Map.entry("A", 8.0),
            Map.entry("B+", 7.0),
            Map.entry("B", 6.0),
            Map.entry("C", 5.0),
            Map.entry("RA", 0.0),
            Map.entry("U", 0.0)
    );

    // ======================================================================
    // GET STUDENT PROFILE
    // ======================================================================
    public UserResponse getProfile(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return convertToUserResponse(user);
    }

    // ======================================================================
    // GET SUBJECTS FOR SEMESTER
    // ======================================================================
    public List<SubjectResponse> getSubjects(Long regulationId, Long departmentId, Integer semesterNumber) {

        if (regulationId == null || departmentId == null || semesterNumber == null) {
            throw new RuntimeException("regulationId, departmentId and semester are required");
        }

        semesterRepository.findByRegulationIdAndDepartmentIdAndNumber(
                regulationId,
                departmentId,
                semesterNumber
        ).orElseThrow(() -> new RuntimeException("Semester not configured"));

        List<Subject> subjects = subjectRepository
                .findByRegulationIdAndDepartmentIdAndSemesterOrderByCodeAsc(
                        regulationId, departmentId, semesterNumber
                );

        if (subjects.isEmpty()) {
            throw new RuntimeException("No subjects found for this semester");
        }

        return subjects.stream()
                .map(this::convertToSubjectResponse)
                .collect(Collectors.toList());
    }

    // ======================================================================
    // GET SEMESTER DETAILS
    // ======================================================================
    public SemesterResponse getSemesterDetails(Long regulationId, Long departmentId, Integer semesterNumber) {

        Semester sem = semesterRepository
                .findByRegulationIdAndDepartmentIdAndNumber(
                        regulationId,
                        departmentId,
                        semesterNumber
                )
                .orElseThrow(() -> new RuntimeException("Semester not found"));

        return new SemesterResponse(
                sem.getId(),
                sem.getNumber(),
                sem.getDescription(),
                sem.getRegulation().getId(),
                sem.getDepartment().getId(),
                sem.getMandatoryCount(),
                sem.getElectiveCount(),
                sem.getActive()
        );
    }

    // ======================================================================
    // CALCULATE & SAVE GPA  (UPSERT LOGIC)
    // ======================================================================
    @Transactional
    public GPAResponse calculateGPA(String email, GPARequest gpaRequest) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        int semester = gpaRequest.getSemester();
        double totalCredits = 0.0;
        double totalPoints = 0.0;

        for (GradeEntryRequest gradeReq : gpaRequest.getSubjects()) {

            Subject subject = subjectRepository.findById(gradeReq.getSubjectId())
                    .orElseThrow(() ->
                            new RuntimeException("Subject not found: " + gradeReq.getSubjectId()));

            // Normalize RA(0) -> U always
            String grade = gradeReq.getGrade();
            if ("RA(0)".equalsIgnoreCase(grade)) grade = "U";

            Double points = gradePoints.get(grade);
            if (points == null) throw new RuntimeException("Invalid grade: " + grade);

            double credits = subject.getCredits();
            totalCredits += credits;
            totalPoints += points * credits;

            // UPSERT using native query
            gradeEntryRepository.upsert(
                    user.getId(),
                    subject.getId(),
                    semester,
                    grade,
                    points
            );
        }

        if (totalCredits == 0) throw new RuntimeException("Cannot compute GPA (credits = 0)");

        double gpa = totalPoints / totalCredits;

        // UPSERT GPA Result (delete existing entry then save)
        gpaResultRepository.findByUserAndSemester(user, semester)
                .ifPresent(gpaResultRepository::delete);

        GpaResult result = new GpaResult(user, semester, gpa, totalCredits, totalPoints);
        gpaResultRepository.save(result);

        return new GPAResponse(gpa, totalCredits, totalPoints);
    }

    // ======================================================================
    // CALCULATE CGPA
    // ======================================================================
    public CGPAResponse calculateCGPA(String email, CGPARequest cgpaRequest) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<GpaResult> results =
                gpaResultRepository.findByUserIdAndSemesterIn(
                        user.getId(),
                        cgpaRequest.getSemesterIds()
                );

        if (results.isEmpty()) throw new RuntimeException("No GPA results found");

        double totalCredits = results.stream().mapToDouble(GpaResult::getTotalCredits).sum();
        double totalPoints = results.stream().mapToDouble(GpaResult::getTotalPoints).sum();
        double cgpa = totalPoints / totalCredits;

        return new CGPAResponse(cgpa, totalCredits, totalPoints);
    }

    // ======================================================================
    // GET GPA HISTORY (DTO FIX - prevents recursion & large JSON)
    // ======================================================================
    public List<GpaHistoryDTO> getHistory(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<GpaResult> list = gpaResultRepository.findByUser(user);

        return list.stream()
                .map(g -> new GpaHistoryDTO(g.getSemester(), g.getGpa()))
                .collect(Collectors.toList());
    }

    // ======================================================================
    // COMPLETED SEMESTERS (used for CGPA summary)
    // ======================================================================
    public Set<Integer> getCompletedSemesters(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<GpaResult> list = gpaResultRepository.findByUser(user);

        return list.stream()
                .map(GpaResult::getSemester)
                .collect(Collectors.toCollection(TreeSet::new));
    }

    // ======================================================================
    // SEMESTER REPORT DATA (for JS PDF – includes grades + grade points)
    // ======================================================================
    public SemesterReportDTO getSemesterReport(String email, Integer semester) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<GradeEntry> entries =
                gradeEntryRepository.findByUserAndSemester(user, semester);

        if (entries.isEmpty()) {
            throw new RuntimeException("No grade entries found for semester " + semester);
        }

        Optional<GpaResult> gpaOpt =
                gpaResultRepository.findByUserAndSemester(user, semester);

        double totalCredits;
        double totalPoints;
        double gpa;

        if (gpaOpt.isPresent()) {
            GpaResult r = gpaOpt.get();
            totalCredits = r.getTotalCredits();
            totalPoints = r.getTotalPoints();
            gpa = r.getGpa();
        } else {
            totalCredits = entries.stream()
                    .mapToDouble(e -> e.getSubject().getCredits())
                    .sum();

            totalPoints = entries.stream()
                    .mapToDouble(e -> e.getPoints() * e.getSubject().getCredits())
                    .sum();

            if (totalCredits == 0) {
                throw new RuntimeException("Cannot compute GPA – totalCredits is 0");
            }
            gpa = totalPoints / totalCredits;
        }

        // Sort by subject code for nice display
        List<SemesterSubjectDTO> subjects = entries.stream()
                .sorted(Comparator.comparing(e -> e.getSubject().getCode()))
                .map(e -> new SemesterSubjectDTO(
                        e.getSubject().getCode(),     // courseCode
                        e.getSubject().getName(),     // courseTitle
                        e.getSubject().getCredits(),  // credits
                        e.getGrade(),                 // grade
                        e.getPoints()                 // gradePoint
                ))
                .collect(Collectors.toList());

        return new SemesterReportDTO(
                semester,
                gpa,
                totalCredits,
                totalPoints,
                subjects
        );
    }

    // ======================================================================
    // CGPA SUMMARY DATA (table of semesters + final CGPA)
    // ======================================================================
    public CGPASummaryDTO getCGPASummaryData(String email, List<Integer> semesterIds) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<GpaResult> results;

        if (semesterIds == null || semesterIds.isEmpty()) {
            // Use all completed semesters
            results = gpaResultRepository.findByUser(user);
        } else {
            results = gpaResultRepository.findByUserIdAndSemesterIn(user.getId(), semesterIds);
        }

        if (results.isEmpty()) {
            throw new RuntimeException("No GPA results found");
        }

        // Sort by semester
        results.sort(Comparator.comparingInt(GpaResult::getSemester));

        double totalCredits = results.stream()
                .mapToDouble(GpaResult::getTotalCredits)
                .sum();

        double totalPoints = results.stream()
                .mapToDouble(GpaResult::getTotalPoints)
                .sum();

        double cgpa = totalPoints / totalCredits;

        List<CGPASemesterDTO> semDtos = results.stream()
                .map(r -> new CGPASemesterDTO(
                        r.getSemester(),
                        r.getGpa(),
                        r.getTotalCredits(),
                        r.getTotalPoints()
                ))
                .collect(Collectors.toList());

        return new CGPASummaryDTO(cgpa, totalCredits, totalPoints, semDtos);
    }

    // ======================================================================
    // DOWNLOAD MARKSHEET PDF (existing backend PDF)
    // ======================================================================
    public byte[] generateMarksheet(String email, Integer semester) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<GradeEntry> gradeEntries =
                gradeEntryRepository.findByUserAndSemester(user, semester);

        Optional<GpaResult> gpaResult =
                gpaResultRepository.findByUserAndSemester(user, semester);

        if (gradeEntries.isEmpty()) {
            throw new RuntimeException("No grade entries found");
        }

        byte[] pdfBytes = pdfService.generateMarksheet(
                user,
                gradeEntries,
                gpaResult.orElse(null),
                semester
        );

        // Save a copy to reports table (non-blocking behaviour)
        try {
            String title = "Marksheet - " + user.getEmail() + " - sem " + semester;
            reportService.saveReport(title, pdfBytes);
        } catch (Exception ex) {
            // Log & continue - do not break the PDF download if report save fails
            System.err.println("Warning: failed to save marksheet report: " + ex.getMessage());
        }

        return pdfBytes;
    }

    // ======================================================================
    // DOWNLOAD CGPA SUMMARY PDF (existing backend PDF)
    // ======================================================================
    public byte[] generateCGPASummary(String email, List<Integer> semIds) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<GpaResult> results =
                gpaResultRepository.findByUserIdAndSemesterIn(user.getId(), semIds);

        if (results.isEmpty()) {
            throw new RuntimeException("No GPA results found");
        }

        byte[] pdfBytes = pdfService.generateCGPASummary(user, results);

        // Save a copy to reports table
        try {
            String title = "CGPA Summary - " + user.getEmail();
            reportService.saveReport(title, pdfBytes);
        } catch (Exception ex) {
            System.err.println("Warning: failed to save cgpa summary report: " + ex.getMessage());
        }

        return pdfBytes;
    }

    // ======================================================================
    // MAPPERS
    // ======================================================================
    private UserResponse convertToUserResponse(User user) {

        UserResponse res = new UserResponse();
        res.setId(user.getId());
        res.setName(user.getName());
        res.setEmail(user.getEmail());
        res.setRole(user.getRole().toString());

        if (user.getRegulation() != null) {
            res.setRegulation(new RegulationResponse(
                    user.getRegulation().getId(),
                    user.getRegulation().getName(),
                    user.getRegulation().getYear()
            ));
        }

        if (user.getDepartment() != null) {
            Department d = user.getDepartment();
            res.setDepartment(new DepartmentResponse(
                    d.getId(),
                    d.getName(),
                    d.getCode(),
                    d.getDescription(),
                    d.getRegulation().getId(),
                    d.getSemesterCount()
            ));
        }

        return res;
    }

    private SubjectResponse convertToSubjectResponse(Subject subject) {
        return new SubjectResponse(
                subject.getId(),
                subject.getCode(),
                subject.getName(),
                subject.getCredits(),
                subject.getIsElective(),
                subject.getType().toString(),
                subject.getSemester()
        );
    }
    // ======================================================================
    // ADMIN: GET STUDENT HISTORY
    // ======================================================================
    public List<GpaHistoryDTO> getHistoryByAdmin(Long studentId) {

        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        return gpaResultRepository.findByUser(student).stream()
                .map(g -> new GpaHistoryDTO(g.getSemester(), g.getGpa()))
                .collect(Collectors.toList());
    }

    // ======================================================================
    // ADMIN: GENERATE MARKSHEET PDF FOR ANY STUDENT
    // ======================================================================
    public byte[] generateMarksheetByAdmin(Long studentId, Integer semester) {

        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        List<GradeEntry> gradeEntries =
                gradeEntryRepository.findByUserAndSemester(student, semester);

        Optional<GpaResult> gpaResult =
                gpaResultRepository.findByUserAndSemester(student, semester);

        if (gradeEntries.isEmpty()) {
            throw new RuntimeException("No grade entries found");
        }

        byte[] pdfBytes = pdfService.generateMarksheet(
                student,
                gradeEntries,
                gpaResult.orElse(null),
                semester
        );

        // Save a copy to reports table
        try {
            String title = "Marksheet (admin) - " + student.getEmail() + " - sem " + semester;
            reportService.saveReport(title, pdfBytes);
        } catch (Exception ex) {
            System.err.println("Warning: failed to save admin marksheet report: " + ex.getMessage());
        }

        return pdfBytes;
    }

    public SemesterReportDTO getSemesterReportByAdmin(Long studentId, Integer semester) {

        User user = userRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        List<GradeEntry> entries =
                gradeEntryRepository.findByUserAndSemester(user, semester);

        if (entries.isEmpty()) {
            throw new RuntimeException("No grade entries found for semester " + semester);
        }

        Optional<GpaResult> gpaOpt =
                gpaResultRepository.findByUserAndSemester(user, semester);

        double totalCredits;
        double totalPoints;
        double gpa;

        if (gpaOpt.isPresent()) {
            GpaResult r = gpaOpt.get();
            totalCredits = r.getTotalCredits();
            totalPoints = r.getTotalPoints();
            gpa = r.getGpa();
        } else {
            totalCredits = entries.stream()
                    .mapToDouble(e -> e.getSubject().getCredits())
                    .sum();

            totalPoints = entries.stream()
                    .mapToDouble(e -> e.getPoints() * e.getSubject().getCredits())
                    .sum();

            gpa = totalPoints / totalCredits;
        }

        List<SemesterSubjectDTO> subjects = entries.stream()
                .sorted(Comparator.comparing(e -> e.getSubject().getCode()))
                .map(e -> new SemesterSubjectDTO(
                        e.getSubject().getCode(),
                        e.getSubject().getName(),
                        e.getSubject().getCredits(),
                        e.getGrade(),
                        e.getPoints()
                ))
                .collect(Collectors.toList());

        return new SemesterReportDTO(semester, gpa, totalCredits, totalPoints, subjects);
    }

}