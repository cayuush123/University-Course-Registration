package com.university.registration.repository;

import com.university.registration.entity.Timetable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TimetableRepository extends JpaRepository<Timetable, Long> {
    List<Timetable> findByCourseCourseId(Long courseId);

    @Query("SELECT t FROM Timetable t WHERE t.course.lecturer.user.email = :email")
    List<Timetable> findTimetablesByLecturerEmail(@Param("email") String email);

    @Query("SELECT DISTINCT t FROM Timetable t JOIN Enrollment e ON e.course.courseId = t.course.courseId WHERE e.student.user.email = :email")
    List<Timetable> findTimetablesByStudentEmail(@Param("email") String email);
}
