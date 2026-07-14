package com.university.registration.repository;

import com.university.registration.entity.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    boolean existsByStudentStudentIdAndCourseCourseId(Long studentId, Long courseId);
}
