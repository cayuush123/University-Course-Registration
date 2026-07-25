package com.university.registration.repository;

import com.university.registration.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    @Query("SELECT c FROM Course c WHERE c.lecturer.user.email = :email")
    List<Course> findByLecturerEmail(@Param("email") String email);

    @Query("SELECT DISTINCT c FROM Course c JOIN Enrollment e ON e.course.courseId = c.courseId WHERE e.student.user.email = :email")
    List<Course> findEnrolledCoursesByStudentEmail(@Param("email") String email);
}
