package com.university.registration.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.university.registration.entity.Grade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface GradeRepository extends JpaRepository<Grade, Long> {
    List<Grade> findByStudentStudentId(Long studentId);
    List<Grade> findByCourseCourseId(Long courseId);
    @Query("SELECT g FROM Grade g WHERE g.student.user.email = :email")
    List<Grade> findGradesByStudentEmail(@Param("email") String email);

//    @Query("SELECT g FROM Grade g WHERE g.student.user.email = :email")
//    List<Grade> findGradesByStudentEmail(@Param("email") String email);

    @Query("SELECT g FROM Grade g WHERE g.course.lecturer.user.email = :email")
    List<Grade> findGradesByLecturerEmail(@Param("email") String email);
}
