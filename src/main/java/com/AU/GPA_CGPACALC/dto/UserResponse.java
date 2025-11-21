package com.AU.GPA_CGPACALC.dto;

public class UserResponse {
    private Long id;
    private String name;
    private String email;
    private String role;
    private RegulationResponse regulation;
    private DepartmentResponse department;

    // Constructors
    public UserResponse() {}

    public UserResponse(Long id, String name, String email, String role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.role = role;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public RegulationResponse getRegulation() { return regulation; }
    public void setRegulation(RegulationResponse regulation) { this.regulation = regulation; }

    public DepartmentResponse getDepartment() { return department; }
    public void setDepartment(DepartmentResponse department) { this.department = department; }
}