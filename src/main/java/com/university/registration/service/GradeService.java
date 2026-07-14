package com.university.registration.service;

import com.university.registration.entity.Grade;
import java.util.List;

public interface GradeService {
    List<Grade> getAllGrades();
    Grade getGradeById(Long id);
    List<Grade> getGradesByStudentId(Long studentId);
    List<Grade> getGradesByCourseId(Long courseId);
    Grade saveGrade(Grade grade);
    Grade updateGrade(Long id, Grade grade);
    void deleteGrade(Long id);
}
