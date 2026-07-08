package com.university.registration.dto;

public class DepartmentResponseDTO {
    private Long departmentId;
    private String departmentName;
    private String faculty;

    public DepartmentResponseDTO() {
    }

    public DepartmentResponseDTO(Long departmentId, String departmentName, String faculty) {
        this.departmentId = departmentId;
        this.departmentName = departmentName;
        this.faculty = faculty;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getFaculty() {
        return faculty;
    }

    public void setFaculty(String faculty) {
        this.faculty = faculty;
    }
}
