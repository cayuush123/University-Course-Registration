package com.university.registration.repository;

import com.university.registration.entity.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

    boolean existsByStudentStudentIdAndCourseCourseId(Long studentId, Long courseId);

    @Query("SELECT e FROM Enrollment e WHERE e.student.user.email = :email")
    List<Enrollment> findEnrollmentsByStudentEmail(@Param("email") String email);

    @Query("SELECT e FROM Enrollment e WHERE e.course.lecturer.user.email = :email")
    List<Enrollment> findEnrollmentsByLecturerEmail(@Param("email") String email);

    @Query("SELECT e FROM Enrollment e WHERE e.course.courseId = :courseId")
    List<Enrollment> findByCourseId(@Param("courseId") Long courseId);

}