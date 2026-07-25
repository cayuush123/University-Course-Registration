package com.university.registration.dto;

import java.time.LocalDate;

public class EnrollmentResponseDTO {
    private Long enrollmentId;
    private StudentResponseDTO student;
    private CourseResponseDTO course;
    private LocalDate enrollmentDate;
    private String status;

    public EnrollmentResponseDTO() {
    }

    public EnrollmentResponseDTO(Long enrollmentId, StudentResponseDTO student, CourseResponseDTO course, LocalDate enrollmentDate, String status) {
        this.enrollmentId = enrollmentId;
        this.student = student;
        this.course = course;
        this.enrollmentDate = enrollmentDate;
        this.status = status;
    }

    public Long getEnrollmentId() {
        return enrollmentId;
    }

    public void setEnrollmentId(Long enrollmentId) {
        this.enrollmentId = enrollmentId;
    }

    public StudentResponseDTO getStudent() {
        return student;
    }

    public void setStudent(StudentResponseDTO student) {
        this.student = student;
    }

    public CourseResponseDTO getCourse() {
        return course;
    }

    public void setCourse(CourseResponseDTO course) {
        this.course = course;
    }

    public LocalDate getEnrollmentDate() {
        return enrollmentDate;
    }

    public void setEnrollmentDate(LocalDate enrollmentDate) {
        this.enrollmentDate = enrollmentDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
