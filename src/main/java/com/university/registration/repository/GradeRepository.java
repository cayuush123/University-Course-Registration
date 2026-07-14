package com.university.registration.repository;

import com.university.registration.entity.Grade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface GradeRepository extends JpaRepository<Grade, Long> {
    List<Grade> findByStudentStudentId(Long studentId);
    List<Grade> findByCourseCourseId(Long courseId);
}
