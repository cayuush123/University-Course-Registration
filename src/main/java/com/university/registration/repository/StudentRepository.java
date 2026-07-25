package com.university.registration.repository;

import com.university.registration.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    @Query("SELECT s FROM Student s WHERE s.user.email = :email")
    Optional<Student> findByUserEmail(@Param("email") String email);

    @Query("SELECT DISTINCT s FROM Student s JOIN Enrollment e ON e.student.studentId = s.studentId WHERE e.course.lecturer.user.email = :email")
    List<Student> findStudentsByLecturerEmail(@Param("email") String email);
}
