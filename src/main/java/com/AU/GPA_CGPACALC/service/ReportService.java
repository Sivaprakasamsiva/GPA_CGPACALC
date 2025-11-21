package com.AU.GPA_CGPACALC.service;

import com.AU.GPA_CGPACALC.dto.ReportResponse;
import com.AU.GPA_CGPACALC.entity.Report;
import com.AU.GPA_CGPACALC.repository.ReportRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReportService {

    private final ReportRepository repo;

    public ReportService(ReportRepository repo) {
        this.repo = repo;
    }

    // ============================
    // RETURN ALL REPORTS
    // ============================
    public List<ReportResponse> getAllReports() {
        return repo.findAll()
                .stream()
                .map(r -> new ReportResponse(
                        r.getId(),
                        r.getTitle(),
                        r.getCreatedAt()
                ))
                .collect(Collectors.toList());
    }

    // ============================
    // DOWNLOAD REPORT PDF
    // ============================
    public byte[] downloadReport(Long id) {
        Report rep = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Report not found"));

        if (rep.getFileData() == null) {
            throw new RuntimeException("PDF file empty");
        }

        return rep.getFileData();
    }

    // ============================
    // COUNT REPORTS (dashboard)
    // ============================
    public long countReports() {
        return repo.count();
    }

    // ============================
    // SAVE A NEW PDF REPORT
    // ============================
    public void saveReport(String title, byte[] pdfBytes) {
        Report r = new Report();
        r.setTitle(title);
        r.setFileData(pdfBytes);
        repo.save(r);
    }
}
