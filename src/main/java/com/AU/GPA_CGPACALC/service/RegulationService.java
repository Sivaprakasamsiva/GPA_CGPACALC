package com.AU.GPA_CGPACALC.service;

import com.AU.GPA_CGPACALC.dto.RegulationRequest;
import com.AU.GPA_CGPACALC.dto.RegulationResponse;
import com.AU.GPA_CGPACALC.entity.Regulation;
import com.AU.GPA_CGPACALC.repository.RegulationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RegulationService {

    private final RegulationRepository regulationRepository;

    public RegulationService(RegulationRepository regulationRepository) {
        this.regulationRepository = regulationRepository;
    }

    public List<RegulationResponse> getAll() {
        return regulationRepository.findAll()
                .stream()
                .map(r -> new RegulationResponse(
                        r.getId(),
                        r.getName(),
                        r.getYear(),
                        r.getDescription()   // NEW 4-arg constructor
                ))
                .collect(Collectors.toList());
    }

    public RegulationResponse create(RegulationRequest req) {
        Regulation r = new Regulation();
        r.setName(req.getName());
        r.setYear(req.getYear());
        r.setDescription(req.getDescription());

        Regulation saved = regulationRepository.save(r);

        return new RegulationResponse(
                saved.getId(),
                saved.getName(),
                saved.getYear(),
                saved.getDescription()
        );
    }

    public RegulationResponse update(Long id, RegulationRequest req) {
        Regulation r = regulationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Regulation not found"));

        r.setName(req.getName());
        r.setYear(req.getYear());
        r.setDescription(req.getDescription());

        Regulation saved = regulationRepository.save(r);

        return new RegulationResponse(
                saved.getId(),
                saved.getName(),
                saved.getYear(),
                saved.getDescription()
        );
    }

    public void delete(Long id) {
        regulationRepository.deleteById(id);
    }

    public long count() {
        return regulationRepository.count();
    }
}
