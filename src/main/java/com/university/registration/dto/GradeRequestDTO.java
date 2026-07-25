package com.university.registration.dto;

public class GradeRequestDTO {
    private Long studentId;
    private Long courseId;
    private Double score;
    private String letterGrade;

    public GradeRequestDTO() {
    }

    public GradeRequestDTO(Long studentId, Long courseId, Double score, String letterGrade) {
        this.studentId = studentId;
        this.courseId = courseId;
        this.score = score;
        this.letterGrade = letterGrade;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
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
