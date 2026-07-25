package com.university.registration.dto;

public class GradeResponseDTO {
    private Long gradeId;
    private StudentResponseDTO student;
    private CourseResponseDTO course;
    private Double score;
    private String letterGrade;

    public GradeResponseDTO() {
    }

    public GradeResponseDTO(Long gradeId, StudentResponseDTO student, CourseResponseDTO course, Double score, String letterGrade) {
        this.gradeId = gradeId;
        this.student = student;
        this.course = course;
        this.score = score;
        this.letterGrade = letterGrade;
    }

    public Long getGradeId() {
        return gradeId;
    }

    public void setGradeId(Long gradeId) {
        this.gradeId = gradeId;
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

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public String getLetterGrade() {
        return letterGrade;
    }

    public void setLetterGrade(String letterGrade) {
        this.letterGrade = letterGrade;
    }
}
