package com.university.registration.dto;

public class DepartmentRequestDTO {
    private String departmentName;
    private String faculty;

    public DepartmentRequestDTO() {
    }

    public DepartmentRequestDTO(String departmentName, String faculty) {
        this.departmentName = departmentName;
        this.faculty = faculty;
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
