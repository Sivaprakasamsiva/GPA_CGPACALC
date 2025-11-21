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
 * AdminService (full) — includes all original methods + safe bulk import helpers.
 * Keep behavior consistent with previous implementation (activation logic, counters, duplicates).
 */
@Service
public class AdminService {

    private static final int DEFAULT_SEM_COUNT = 8; // default semesters when creating department

    @Autowired private RegulationRepository regulationRepository;
    @Autowired private DepartmentRepository departmentRepository;
    @Autowired private SemesterRepository semesterRepository;
    @Autowired private SubjectRepository subjectRepository;

    // ---------- Regulations ----------
    public List<RegulationResponse> getRegulations() {
        return regulationRepository.findAll().stream()
                .map(r -> new RegulationResponse(r.getId(), r.getName(), r.getYear()))
                .collect(Collectors.toList());
    }

    // add near other helpers in AdminService
    public long countDepartments() {
        return departmentRepository.count();
    }

    public long countRegulations() {
        return regulationRepository.count();
    }

    public RegulationResponse createRegulation(Regulation req) {
        Regulation r = new Regulation();
        r.setName(req.getName());
        r.setYear(req.getYear());
        r.setDescription(req.getDescription());
        Regulation saved = regulationRepository.save(r);
        return new RegulationResponse(saved.getId(), saved.getName(), saved.getYear());
    }

    public RegulationResponse updateRegulation(Long id, Regulation req) {
        Regulation r = regulationRepository.findById(id).orElseThrow(() -> new RuntimeException("Regulation not found"));
        if (req.getName() != null) r.setName(req.getName());
        if (req.getYear() != null) r.setYear(req.getYear());
        if (req.getDescription() != null) r.setDescription(req.getDescription());
        Regulation saved = regulationRepository.save(r);
        return new RegulationResponse(saved.getId(), saved.getName(), saved.getYear());
    }

    public void deleteRegulation(Long id) {
        // For safety: only allow deleting when no departments attached (or cascade as wanted).
        List<Department> deps = departmentRepository.findAll().stream().filter(d -> d.getRegulation() != null && d.getRegulation().getId().equals(id)).collect(Collectors.toList());
        if (!deps.isEmpty()) throw new RuntimeException("Cannot delete regulation with departments. Remove departments first.");
        regulationRepository.deleteById(id);
    }

    // ---------- Departments ----------
    public List<DepartmentResponse> getDepartments() {
        return departmentRepository.findAll().stream().map(d -> {
            Long regId = d.getRegulation() != null ? d.getRegulation().getId() : null;
            return new DepartmentResponse(d.getId(), d.getName(), d.getCode(), d.getDescription(), d.getSemesterCount(), regId);
        }).collect(Collectors.toList());
    }

    @Transactional
    public DepartmentResponse createDepartment(DepartmentRequest req) {
        if (req.getCode() == null || req.getCode().isBlank()) throw new RuntimeException("Code required");
        if (departmentRepository.existsByCode(req.getCode())) throw new RuntimeException("Department code exists");

        Regulation reg = regulationRepository.findById(req.getRegulationId())
                .orElseThrow(() -> new RuntimeException("Regulation not found"));

        Department d = new Department();
        d.setName(req.getName());
        d.setCode(req.getCode());
        d.setDescription(req.getDescription());
        d.setRegulation(reg);
        int sc = Optional.ofNullable(req.getSemesterCount()).orElse(DEFAULT_SEM_COUNT);
        d.setSemesterCount(sc);
        Department saved = departmentRepository.save(d);

        // create semesters for this department+regulation
        ensureSemestersForDepartmentRegulation(saved, reg, sc);

        return new DepartmentResponse(saved.getId(), saved.getName(), saved.getCode(), saved.getDescription(), saved.getSemesterCount(), reg.getId());
    }

    @Transactional
    public DepartmentResponse updateDepartment(Long id, DepartmentRequest req) {
        Department d = departmentRepository.findById(id).orElseThrow(() -> new RuntimeException("Department not found"));
        if (req.getName() != null) d.setName(req.getName());
        if (req.getCode() != null && !req.getCode().equals(d.getCode())) {
            if (departmentRepository.existsByCode(req.getCode())) throw new RuntimeException("Department code exists");
            d.setCode(req.getCode());
        }
        if (req.getDescription() != null) d.setDescription(req.getDescription());

        Integer oldCount = d.getSemesterCount();
        if (req.getSemesterCount() != null) d.setSemesterCount(req.getSemesterCount());

        Department saved = departmentRepository.save(d);

        Integer newCount = saved.getSemesterCount();
        if (!Objects.equals(oldCount, newCount)) {
            Regulation reg = saved.getRegulation();
            if (reg != null) syncSemestersForCount(saved, reg, newCount == null ? 0 : newCount);
        }

        Long regId = saved.getRegulation() != null ? saved.getRegulation().getId() : null;
        return new DepartmentResponse(saved.getId(), saved.getName(), saved.getCode(), saved.getDescription(), saved.getSemesterCount(), regId);
    }

    /**
     * HARD delete department as requested (B):
     * - delete subjects for department
     * - delete semesters for department
     * - delete department
     */
    @Transactional
    public void deleteDepartment(Long id) {
        Department dept = departmentRepository.findById(id).orElseThrow(() -> new RuntimeException("Department not found"));

        // delete subjects (hard delete)
        List<Subject> subjects = subjectRepository.findByDepartmentId(id);
        if (!subjects.isEmpty()) subjectRepository.deleteAll(subjects);

        // delete semesters (hard delete)
        List<Semester> sems = semesterRepository.findByDepartmentId(id);
        if (!sems.isEmpty()) semesterRepository.deleteAll(sems);

        // finally delete department
        departmentRepository.delete(dept);
    }

    // ---------- Semesters ----------
    public List<SemesterResponse> getSemesters(Long regulationId, Long departmentId) {
        if (regulationId == null || departmentId == null) return List.of();
        List<Semester> sems = semesterRepository.findByRegulationIdAndDepartmentIdOrderByNumberAsc(regulationId, departmentId);
        return sems.stream().map(s -> new SemesterResponse(
                s.getId(), s.getNumber(), s.getDescription(),
                s.getRegulation().getId(), s.getDepartment().getId(),
                s.getMandatoryCount(), s.getElectiveCount(), s.getActive()
        )).filter(SemesterResponse::getActive).collect(Collectors.toList());
    }

    @Transactional
    public SemesterResponse createSemester(SemesterRequest req) {
        Regulation r = regulationRepository.findById(req.getRegulationId()).orElseThrow(() -> new RuntimeException("Regulation not found"));
        Department d = departmentRepository.findById(req.getDepartmentId()).orElseThrow(() -> new RuntimeException("Department not found"));

        Integer number = req.getNumber();
        if (number == null || number <= 0) throw new RuntimeException("Invalid semester number");

        Optional<Semester> existing = semesterRepository.findByRegulationIdAndDepartmentIdAndNumber(r.getId(), d.getId(), number);
        if (existing.isPresent()) {
            Semester s = existing.get();
            if (Boolean.TRUE.equals(s.getActive())) throw new RuntimeException("Semester already exists");
            s.setActive(true);
            s.setDescription(req.getDescription());
            s.setMandatoryCount(Optional.ofNullable(req.getMandatoryCount()).orElse(s.getMandatoryCount()));
            s.setElectiveCount(Optional.ofNullable(req.getElectiveCount()).orElse(s.getElectiveCount()));
            semesterRepository.save(s);
            return new SemesterResponse(s.getId(), s.getNumber(), s.getDescription(), r.getId(), d.getId(), s.getMandatoryCount(), s.getElectiveCount(), s.getActive());
        } else {
            Semester s = new Semester();
            s.setNumber(number);
            s.setDescription(req.getDescription());
            s.setRegulation(r);
            s.setDepartment(d);
            s.setMandatoryCount(Optional.ofNullable(req.getMandatoryCount()).orElse(0));
            s.setElectiveCount(Optional.ofNullable(req.getElectiveCount()).orElse(0));
            s.setActive(true);
            Semester saved = semesterRepository.save(s);
            return new SemesterResponse(saved.getId(), saved.getNumber(), saved.getDescription(), r.getId(), d.getId(), saved.getMandatoryCount(), saved.getElectiveCount(), saved.getActive());
        }
    }

    @Transactional
    public SemesterResponse updateSemester(Long id, SemesterRequest req) {
        Semester s = semesterRepository.findById(id).orElseThrow(() -> new RuntimeException("Semester not found"));
        if (req.getNumber() != null && !req.getNumber().equals(s.getNumber())) {
            if (semesterRepository.existsByRegulationIdAndDepartmentIdAndNumber(s.getRegulation().getId(), s.getDepartment().getId(), req.getNumber())) {
                throw new RuntimeException("Duplicate semester number");
            }
            s.setNumber(req.getNumber());
        }
        if (req.getDescription() != null) s.setDescription(req.getDescription());
        if (req.getMandatoryCount() != null) s.setMandatoryCount(req.getMandatoryCount());
        if (req.getElectiveCount() != null) s.setElectiveCount(req.getElectiveCount());
        if (req.getActive() != null) s.setActive(req.getActive());
        Semester updated = semesterRepository.save(s);
        return new SemesterResponse(updated.getId(), updated.getNumber(), updated.getDescription(), updated.getRegulation().getId(), updated.getDepartment().getId(), updated.getMandatoryCount(), updated.getElectiveCount(), updated.getActive());
    }

    @Transactional
    public void deleteSemester(Long id) {
        Semester s = semesterRepository.findById(id).orElseThrow(() -> new RuntimeException("Semester not found"));
        semesterRepository.delete(s); // hard delete semester
    }

    // ---------- Subjects ----------
    @Transactional
    public SubjectResponse addSubject(SubjectRequest req) {
        Regulation reg = regulationRepository.findById(req.getRegulationId()).orElseThrow(() -> new RuntimeException("Regulation not found"));
        Department dep = departmentRepository.findById(req.getDepartmentId()).orElseThrow(() -> new RuntimeException("Department not found"));

        if (subjectRepository.existsByCodeAndRegulationIdAndDepartmentIdAndSemester(req.getCode(), req.getRegulationId(), req.getDepartmentId(), req.getSemester())) {
            throw new RuntimeException("Subject exists");
        }

        Subject s = new Subject();
        s.setCode(req.getCode());
        s.setName(req.getName());
        s.setCredits(Optional.ofNullable(req.getCredits()).orElse(0.0));
        s.setIsElective(Optional.ofNullable(req.getIsElective()).orElse(false));
        if (req.getType() != null) s.setType(SubjectType.valueOf(req.getType()));
        s.setRegulation(reg);
        s.setDepartment(dep);
        s.setSemester(req.getSemester());
        Subject saved = subjectRepository.save(s);

        // update semester counters
        adjustCountersAfterCreate(saved);
        return new SubjectResponse(saved.getId(), saved.getCode(), saved.getName(), saved.getCredits(), saved.getIsElective(), saved.getType().toString(), saved.getSemester());
    }

    @Transactional
    public SubjectResponse updateSubject(Long id, SubjectRequest req) {
        Subject s = subjectRepository.findById(id).orElseThrow(() -> new RuntimeException("Subject not found"));
        Long oldReg = s.getRegulation().getId();
        Long oldDept = s.getDepartment().getId();
        Integer oldSem = s.getSemester();
        Boolean oldElective = s.getIsElective();

        if (req.getCode() != null) s.setCode(req.getCode());
        if (req.getName() != null) s.setName(req.getName());
        if (req.getCredits() != null) s.setCredits(req.getCredits());
        if (req.getIsElective() != null) s.setIsElective(req.getIsElective());
        if (req.getType() != null) s.setType(SubjectType.valueOf(req.getType()));
        if (req.getSemester() != null) s.setSemester(req.getSemester());
        if (req.getRegulationId() != null && !req.getRegulationId().equals(oldReg)) {
            Regulation r = regulationRepository.findById(req.getRegulationId()).orElseThrow(() -> new RuntimeException("Regulation not found"));
            s.setRegulation(r);
        }
        if (req.getDepartmentId() != null && !req.getDepartmentId().equals(oldDept)) {
            Department d = departmentRepository.findById(req.getDepartmentId()).orElseThrow(() -> new RuntimeException("Department not found"));
            s.setDepartment(d);
        }

        Subject updated = subjectRepository.save(s);

        adjustSemesterCountersAfterSubjectChange(oldReg, oldDept, oldSem, oldElective,
                updated.getRegulation().getId(), updated.getDepartment().getId(), updated.getSemester(), updated.getIsElective());

        return new SubjectResponse(updated.getId(), updated.getCode(), updated.getName(), updated.getCredits(), updated.getIsElective(), updated.getType().toString(), updated.getSemester());
    }

    @Transactional
    public void deleteSubject(Long id) {
        Subject s = subjectRepository.findById(id).orElseThrow(() -> new RuntimeException("Subject not found"));
        adjustSemesterCountersAfterSubjectChange(s.getRegulation().getId(), s.getDepartment().getId(), s.getSemester(), s.getIsElective(), null, null, null, null);
        subjectRepository.delete(s); // hard delete as requested
    }

    public List<SubjectResponse> getSubjects(Long regId, Long deptId, Integer semester) {
        List<Subject> lst = subjectRepository.findFiltered(regId, deptId, semester);
        return lst.stream().map(s -> new SubjectResponse(s.getId(), s.getCode(), s.getName(), s.getCredits(), s.getIsElective(), s.getType().toString(), s.getSemester())).collect(Collectors.toList());
    }

    // ---------------- Helpers ----------------

    private void adjustCountersAfterCreate(Subject saved) {
        Optional<Semester> semOpt = semesterRepository.findByRegulationIdAndDepartmentIdAndNumber(
                saved.getRegulation().getId(), saved.getDepartment().getId(), saved.getSemester());
        if (semOpt.isPresent()) {
            Semester sem = semOpt.get();
            if (Boolean.TRUE.equals(saved.getIsElective())) sem.setElectiveCount(sem.getElectiveCount() + 1);
            else sem.setMandatoryCount(sem.getMandatoryCount() + 1);
            semesterRepository.save(sem);
        }
    }

    private void adjustSemesterCountersAfterSubjectChange(Long oldRegId, Long oldDeptId, Integer oldSem, Boolean oldElective,
                                                          Long newRegId, Long newDeptId, Integer newSem, Boolean newElective) {
        // decrement old
        if (oldRegId != null && oldDeptId != null && oldSem != null) {
            Optional<Semester> oldOpt = semesterRepository.findByRegulationIdAndDepartmentIdAndNumber(oldRegId, oldDeptId, oldSem);
            if (oldOpt.isPresent()) {
                Semester ost = oldOpt.get();
                if (Boolean.TRUE.equals(oldElective)) ost.setElectiveCount(Math.max(0, ost.getElectiveCount() - 1));
                else ost.setMandatoryCount(Math.max(0, ost.getMandatoryCount() - 1));
                semesterRepository.save(ost);
            }
        }
        // increment new
        if (newRegId != null && newDeptId != null && newSem != null) {
            Optional<Semester> newOpt = semesterRepository.findByRegulationIdAndDepartmentIdAndNumber(newRegId, newDeptId, newSem);
            if (newOpt.isPresent()) {
                Semester nst = newOpt.get();
                if (Boolean.TRUE.equals(newElective)) nst.setElectiveCount(nst.getElectiveCount() + 1);
                else nst.setMandatoryCount(nst.getMandatoryCount() + 1);
                semesterRepository.save(nst);
            }
        }
    }

    private void ensureSemestersForDepartmentRegulation(Department dep, Regulation reg, int count) {
        if (count <= 0) return;
        for (int i = 1; i <= count; i++) {
            Optional<Semester> existing = semesterRepository.findByRegulationIdAndDepartmentIdAndNumber(reg.getId(), dep.getId(), i);
            if (existing.isPresent()) {
                Semester s = existing.get();
                if (!Boolean.TRUE.equals(s.getActive())) {
                    s.setActive(true);
                    semesterRepository.save(s);
                }
            } else {
                Semester s = new Semester();
                s.setNumber(i);
                s.setDescription(null);
                s.setRegulation(reg);
                s.setDepartment(dep);
                s.setMandatoryCount(0);
                s.setElectiveCount(0);
                s.setActive(true);
                semesterRepository.save(s);
            }
        }
    }

    private void syncSemestersForCount(Department dep, Regulation reg, int newCount) {
        List<Semester> sems = semesterRepository.findByRegulationIdAndDepartmentIdOrderByNumberAsc(reg.getId(), dep.getId());
        int maxExisting = sems.stream().mapToInt(Semester::getNumber).max().orElse(0);
        if (newCount > maxExisting) {
            for (int num = maxExisting + 1; num <= newCount; num++) {
                Semester s = new Semester();
                s.setNumber(num);
                s.setRegulation(reg);
                s.setDepartment(dep);
                s.setDescription(null);
                s.setMandatoryCount(0);
                s.setElectiveCount(0);
                s.setActive(true);
                semesterRepository.save(s);
            }
        } else if (newCount < maxExisting) {
            for (Semester s : sems) {
                if (s.getNumber() > newCount) {
                    // hard-delete the extra semesters and their subjects
                    List<Subject> subs = subjectRepository.findByRegulationIdAndDepartmentIdAndSemester(s.getRegulation().getId(), s.getDepartment().getId(), s.getNumber());
                    if (!subs.isEmpty()) subjectRepository.deleteAll(subs);
                    semesterRepository.delete(s);
                }
            }
        }
    }

    // ============================
    // BULK IMPORT IMPLEMENTATION
    // ============================

    /**
     * Import structured JSON with semesters and subjects.
     * Transactional: if any error occurs for a row we record it and continue; entire import is in same transaction
     * so thrown runtime exceptions will rollback all changes. The code here catches row-level errors and records them
     * so a partial import remains possible if that is desired; adjust behavior by throwing for fatal errors.
     */
    @Transactional
    public ImportResultDTO importBulk(BulkImportRequest bulk) {
        ImportResultDTO result = new ImportResultDTO();
        if (bulk == null) {
            result.addError("request", "Request body is empty");
            return result;
        }

        // Import semesters first (so subjects referencing them will find them)
        List<SemesterImportDTO> sems = Optional.ofNullable(bulk.getSemesters()).orElse(Collections.emptyList());
        for (int i = 0; i < sems.size(); i++) {
            SemesterImportDTO si = sems.get(i);
            String rowId = "semester[" + i + "]";
            try {
                validateSemesterImport(si, rowId);
                // Create or activate using existing createSemester logic
                SemesterRequest req = new SemesterRequest();
                req.setRegulationId(si.getRegulationId());
                req.setDepartmentId(si.getDepartmentId());
                req.setNumber(si.getNumber());
                req.setDescription(si.getDescription());
                req.setMandatoryCount(si.getMandatoryCount());
                req.setElectiveCount(si.getElectiveCount());
                // reuse createSemester which will activate existing inactive semester
                createSemester(req);
                result.incrementSemestersCreated();
            } catch (Exception ex) {
                result.addError(rowId, ex.getMessage());
            }
        }

        // Import subjects
        List<SubjectImportDTO> subs = Optional.ofNullable(bulk.getSubjects()).orElse(Collections.emptyList());
        for (int i = 0; i < subs.size(); i++) {
            SubjectImportDTO s = subs.get(i);
            String rowId = "subject[" + i + "]";
            try {
                validateSubjectImport(s, rowId);

                // ensure regulation and department exist
                Regulation reg = regulationRepository.findById(s.getRegulationId())
                        .orElseThrow(() -> new RuntimeException("Regulation not found (id=" + s.getRegulationId() + ")"));
                Department dep = departmentRepository.findById(s.getDepartmentId())
                        .orElseThrow(() -> new RuntimeException("Department not found (id=" + s.getDepartmentId() + ")"));

                // ensure semester exists (create if missing)
                Optional<Semester> semOpt = semesterRepository.findByRegulationIdAndDepartmentIdAndNumber(reg.getId(), dep.getId(), s.getSemester());
                if (semOpt.isEmpty()) {
                    // create new semester with sensible defaults
                    Semester sem = new Semester();
                    sem.setRegulation(reg);
                    sem.setDepartment(dep);
                    sem.setNumber(s.getSemester());
                    sem.setActive(true);
                    sem.setDescription(null);
                    sem.setMandatoryCount(0);
                    sem.setElectiveCount(0);
                    semesterRepository.save(sem);
                }

                // check duplicate subject unique constraint
                if (subjectRepository.existsByCodeAndRegulationIdAndDepartmentIdAndSemester(s.getCode(), s.getRegulationId(), s.getDepartmentId(), s.getSemester())) {
                    throw new RuntimeException("Subject already exists: code=" + s.getCode());
                }

                // create subject
                Subject subj = new Subject();
                subj.setCode(s.getCode());
                subj.setName(s.getName());
                subj.setCredits(Optional.ofNullable(s.getCredits()).orElse(0.0));
                subj.setIsElective(Optional.ofNullable(s.getIsElective()).orElse(false));
                if (s.getType() != null) subj.setType(SubjectType.valueOf(s.getType()));
                subj.setRegulation(reg);
                subj.setDepartment(dep);
                subj.setSemester(s.getSemester());

                Subject saved = subjectRepository.save(subj);
                // update semester counters
                adjustCountersAfterCreate(saved);

                result.incrementSubjectsCreated();

            } catch (Exception ex) {
                result.addError(rowId, ex.getMessage());
            }
        }

        return result;
    }

    /**
     * Very simple CSV parser for convenience. Format is not strict here — recommended approach: use structured JSON import.
     * This method supports lines that start with "SEM" or "SUB" and comma-separated fields.
     *
     * Example:
     * SEM,regulationId,departmentId,number,description,mandatory,elective
     * SUB,code,name,credits,isElective,type,regulationId,departmentId,semester
     */
    @Transactional
    public ImportResultDTO importFromCsv(String csvText) {
        ImportResultDTO res = new ImportResultDTO();
        if (csvText == null || csvText.isBlank()) {
            res.addError("csv", "CSV body is empty");
            return res;
        }

        String[] lines = csvText.split("\\r?\\n");
        List<SemesterImportDTO> sems = new ArrayList<>();
        List<SubjectImportDTO> subs = new ArrayList<>();

        for (int i = 0; i < lines.length; i++) {
            String raw = lines[i].trim();
            if (raw.isEmpty()) continue;
            String[] parts = raw.split(",");
            String tag = parts[0].trim().toUpperCase();
            try {
                if ("SEM".equals(tag)) {
                    // SEM,regulationId,departmentId,number,description,mandatory,elective
                    if (parts.length < 4) throw new RuntimeException("SEM row requires at least regulationId,departmentId,number");
                    SemesterImportDTO si = new SemesterImportDTO();
                    si.setRegulationId(Long.parseLong(parts[1].trim()));
                    si.setDepartmentId(Long.parseLong(parts[2].trim()));
                    si.setNumber(Integer.parseInt(parts[3].trim()));
                    si.setDescription(parts.length > 4 ? parts[4].trim() : null);
                    if (parts.length > 5) si.setMandatoryCount(Integer.parseInt(parts[5].trim()));
                    if (parts.length > 6) si.setElectiveCount(Integer.parseInt(parts[6].trim()));
                    sems.add(si);
                } else if ("SUB".equals(tag)) {
                    // SUB,code,name,credits,isElective,type,regulationId,departmentId,semester
                    if (parts.length < 9) throw new RuntimeException("SUB row requires 9 columns");
                    SubjectImportDTO si = new SubjectImportDTO();
                    si.setCode(parts[1].trim());
                    si.setName(parts[2].trim());
                    si.setCredits(Double.parseDouble(parts[3].trim()));
                    si.setIsElective(Boolean.parseBoolean(parts[4].trim()));
                    si.setType(parts[5].trim());
                    si.setRegulationId(Long.parseLong(parts[6].trim()));
                    si.setDepartmentId(Long.parseLong(parts[7].trim()));
                    si.setSemester(Integer.parseInt(parts[8].trim()));
                    subs.add(si);
                } else {
                    res.addError("line[" + i + "]", "Unknown record type: " + tag);
                }
            } catch (Exception ex) {
                res.addError("line[" + i + "]", ex.getMessage());
            }
        }

        BulkImportRequest req = new BulkImportRequest();
        req.setSemesters(sems);
        req.setSubjects(subs);

        // delegate to importBulk for actual application
        ImportResultDTO result = importBulk(req);
        return result;
    }

    // validation helpers
    private void validateSemesterImport(SemesterImportDTO si, String rowId) {
        if (si.getRegulationId() == null) throw new RuntimeException(rowId + ": regulationId required");
        if (si.getDepartmentId() == null) throw new RuntimeException(rowId + ": departmentId required");
        if (si.getNumber() == null || si.getNumber() <= 0) throw new RuntimeException(rowId + ": invalid semester number");
    }

    private void validateSubjectImport(SubjectImportDTO s, String rowId) {
        if (s.getCode() == null || s.getCode().isBlank()) throw new RuntimeException(rowId + ": code required");
        if (s.getName() == null || s.getName().isBlank()) throw new RuntimeException(rowId + ": name required");
        if (s.getRegulationId() == null) throw new RuntimeException(rowId + ": regulationId required");
        if (s.getDepartmentId() == null) throw new RuntimeException(rowId + ": departmentId required");
        if (s.getSemester() == null || s.getSemester() <= 0) throw new RuntimeException(rowId + ": semester required");
    }

    /**
     * Backwards-compatible helper used by frontend when sending a simple list of semesters.
     * This method solely inserts Semester records (skips duplicates).
     */
    @Transactional
    public void bulkImportSemesters(List<BulkSemesterDTO> list) {
        if (list == null) return;
        for (BulkSemesterDTO dto : list) {

            if (dto == null) continue;

            if (dto.getRegulationId() == null || dto.getDepartmentId() == null || dto.getNumber() == null) {
                // skip invalid lines; could also throw if you prefer strict behavior
                continue;
            }

            if (semesterRepository
                    .existsByRegulationIdAndDepartmentIdAndNumber(
                            dto.getRegulationId(),
                            dto.getDepartmentId(),
                            dto.getNumber()
                    )) {
                continue; // skip duplicates
            }

            Semester sem = new Semester();
            sem.setNumber(dto.getNumber());
            sem.setDescription(dto.getDescription());
            sem.setMandatoryCount(Optional.ofNullable(dto.getMandatoryCount()).orElse(0));
            sem.setElectiveCount(Optional.ofNullable(dto.getElectiveCount()).orElse(0));

            Regulation reg = regulationRepository.findById(dto.getRegulationId())
                    .orElseThrow(() -> new RuntimeException("Regulation not found"));

            Department dep = departmentRepository.findById(dto.getDepartmentId())
                    .orElseThrow(() -> new RuntimeException("Department not found"));

            sem.setRegulation(reg);
            sem.setDepartment(dep);
            sem.setActive(true);

            semesterRepository.save(sem);
        }
    }
}
