package com.AU.GPA_CGPACALC.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "subjects", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"code", "regulation_id", "department_id", "semester"})
})
public class Subject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String code;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Double credits;

    private Boolean isElective = false;

    @Enumerated(EnumType.STRING)
    private SubjectType type = SubjectType.CORE; // default value

    @ManyToOne
    @JoinColumn(name = "regulation_id", nullable = false)
    private Regulation regulation;

    @ManyToOne
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;

    @Column(nullable = false)
    private Integer semester;

    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "subject", cascade = CascadeType.ALL)
    private List<GradeEntry> gradeEntries;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    // Constructors
    public Subject() {}

    public Subject(String code, String name, Double credits, Regulation regulation,
                   Department department, Integer semester) {
        this.code = code;
        this.name = name;
        this.credits = credits;
        this.regulation = regulation;
        this.department = department;
        this.semester = semester;
        // ensure type is not null; default to CORE if unset
        if (this.type == null) this.type = SubjectType.CORE;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Double getCredits() { return credits; }
    public void setCredits(Double credits) { this.credits = credits; }

    public Boolean getIsElective() { return isElective; }
    public void setIsElective(Boolean isElective) { this.isElective = isElective; }

    public SubjectType getType() { return type; }
    public void setType(SubjectType type) { this.type = type; }

    public Regulation getRegulation() { return regulation; }
    public void setRegulation(Regulation regulation) { this.regulation = regulation; }

    public Department getDepartment() { return department; }
    public void setDepartment(Department department) { this.department = department; }

    public Integer getSemester() { return semester; }
    public void setSemester(Integer semester) { this.semester = semester; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public List<GradeEntry> getGradeEntries() { return gradeEntries; }
    public void setGradeEntries(List<GradeEntry> gradeEntries) { this.gradeEntries = gradeEntries; }
}
