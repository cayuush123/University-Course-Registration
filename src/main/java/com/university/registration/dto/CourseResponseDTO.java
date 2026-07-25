package com.university.registration.dto;

public class CourseResponseDTO {
    private Long courseId;
    private String courseCode;
    private String courseName;
    private Integer credits;
    private DepartmentResponseDTO department;
    private LecturerResponseDTO lecturer;

    public CourseResponseDTO() {
    }

    public CourseResponseDTO(Long courseId, String courseCode, String courseName, Integer credits, DepartmentResponseDTO department, LecturerResponseDTO lecturer) {
        this.courseId = courseId;
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.credits = credits;
        this.department = department;
        this.lecturer = lecturer;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public Integer getCredits() {
        return credits;
    }

    public void setCredits(Integer credits) {
        this.credits = credits;
    }

    public DepartmentResponseDTO getDepartment() {
        return department;
    }

    public void setDepartment(DepartmentResponseDTO department) {
        this.department = department;
    }

    public LecturerResponseDTO getLecturer() {
        return lecturer;
    }

    public void setLecturer(LecturerResponseDTO lecturer) {
        this.lecturer = lecturer;
    }
}
