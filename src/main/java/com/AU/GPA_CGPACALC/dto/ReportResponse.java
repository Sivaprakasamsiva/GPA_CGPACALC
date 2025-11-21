package com.AU.GPA_CGPACALC.dto;

import java.time.LocalDateTime;

public class ReportResponse {
    private Long id;
    private String title;
    private LocalDateTime createdAt;

    public ReportResponse() {}

    public ReportResponse(Long id, String title, LocalDateTime createdAt) {
        this.id = id;
        this.title = title;
        this.createdAt = createdAt;
    }

    // getters/setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
