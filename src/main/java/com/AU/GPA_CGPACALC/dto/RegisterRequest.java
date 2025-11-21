package com.AU.GPA_CGPACALC.dto;

public class RegisterRequest {

    private String name;
    private String email;
    private String password;

    private Long regulationId;
    private Long departmentId;

    public RegisterRequest() {}

    public RegisterRequest(String name,
                           String email,
                           String password,
                           Long regulationId,
                           Long departmentId) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.regulationId = regulationId;
        this.departmentId = departmentId;
    }

    // GETTERS & SETTERS

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long getRegulationId() {
        return regulationId;
    }

    public void setRegulationId(Long regulationId) {
        this.regulationId = regulationId;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }
}
