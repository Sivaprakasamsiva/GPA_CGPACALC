package com.AU.GPA_CGPACALC.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "semesters", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"regulation_id", "department_id", "number"})
})
public class Semester {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer number;

    @Column(length = 500)
    private String description;

    @ManyToOne
    @JoinColumn(name = "regulation_id", nullable = false)
    private Regulation regulation;

    @ManyToOne
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;

    @Column(nullable = false)
    private Integer mandatoryCount = 0;

    @Column(nullable = false)
    private Integer electiveCount = 0;

    @Column(nullable = false)
    private Boolean active = true; // soft delete toggle

    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    // getters/setters below...
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Integer getNumber() { return number; }
    public void setNumber(Integer number) { this.number = number; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Regulation getRegulation() { return regulation; }
    public void setRegulation(Regulation regulation) { this.regulation = regulation; }
    public Department getDepartment() { return department; }
    public void setDepartment(Department department) { this.department = department; }
    public Integer getMandatoryCount() { return mandatoryCount; }
    public void setMandatoryCount(Integer mandatoryCount) { this.mandatoryCount = mandatoryCount; }
    public Integer getElectiveCount() { return electiveCount; }
    public void setElectiveCount(Integer electiveCount) { this.electiveCount = electiveCount; }
    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
