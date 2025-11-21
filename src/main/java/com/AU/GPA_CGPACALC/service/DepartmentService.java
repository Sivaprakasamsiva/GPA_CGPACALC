package com.AU.GPA_CGPACALC.service;

import com.AU.GPA_CGPACALC.dto.DepartmentRequest;
import com.AU.GPA_CGPACALC.dto.DepartmentResponse;
import com.AU.GPA_CGPACALC.entity.Department;
import com.AU.GPA_CGPACALC.repository.DepartmentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DepartmentService {

    private final DepartmentRepository departmentRepository;

    public DepartmentService(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    public List<DepartmentResponse> getAll() {
        return departmentRepository.findAll().stream()
                .map(d -> new DepartmentResponse(
                        d.getId(),
                        d.getName(),
                        d.getCode(),
                        d.getDescription(),
                        d.getSemesterCount()  // 5-arg constructor
                ))
                .collect(Collectors.toList());
    }

    public long count() {
        return departmentRepository.count();
    }

    public DepartmentResponse create(DepartmentRequest req) {
        Department d = new Department();
        d.setName(req.getName());
        d.setCode(req.getCode());
        d.setDescription(req.getDescription());
        d.setSemesterCount(req.getSemesterCount());

        Department saved = departmentRepository.save(d);

        return new DepartmentResponse(
                saved.getId(),
                saved.getName(),
                saved.getCode(),
                saved.getDescription(),
                saved.getSemesterCount()
        );
    }

    public DepartmentResponse update(Long id, DepartmentRequest req) {
        Department d = departmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Department not found"));

        d.setName(req.getName());
        d.setCode(req.getCode());
        d.setDescription(req.getDescription());
        d.setSemesterCount(req.getSemesterCount());

        Department saved = departmentRepository.save(d);

        return new DepartmentResponse(
                saved.getId(),
                saved.getName(),
                saved.getCode(),
                saved.getDescription(),
                saved.getSemesterCount()
        );
    }

    public void delete(Long id) {
        departmentRepository.deleteById(id);
    }
}
