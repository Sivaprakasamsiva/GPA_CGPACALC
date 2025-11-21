package com.AU.GPA_CGPACALC.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "gpa_results")
public class GpaResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private Integer semester;

    @Column(nullable = false)
    private Double gpa;

    @Column(nullable = false)
    private Double totalCredits;

    @Column(nullable = false)
    private Double totalPoints;

    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    // Constructors
    public GpaResult() {}

    public GpaResult(User user, Integer semester, Double gpa, Double totalCredits, Double totalPoints) {
        this.user = user;
        this.semester = semester;
        this.gpa = gpa;
        this.totalCredits = totalCredits;
        this.totalPoints = totalPoints;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Integer getSemester() { return semester; }
    public void setSemester(Integer semester) { this.semester = semester; }

    public Double getGpa() { return gpa; }
    public void setGpa(Double gpa) { this.gpa = gpa; }

    public Double getTotalCredits() { return totalCredits; }
    public void setTotalCredits(Double totalCredits) { this.totalCredits = totalCredits; }

    public Double getTotalPoints() { return totalPoints; }
    public void setTotalPoints(Double totalPoints) { this.totalPoints = totalPoints; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}