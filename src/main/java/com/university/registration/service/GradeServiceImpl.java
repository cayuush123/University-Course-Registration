package com.university.registration.service;

import com.university.registration.entity.Grade;
import com.university.registration.entity.Student;
import com.university.registration.entity.Course;
import com.university.registration.exception.ResourceNotFoundException;
import com.university.registration.repository.GradeRepository;
import com.university.registration.repository.StudentRepository;
import com.university.registration.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class GradeServiceImpl implements GradeService {

    @Autowired
    private GradeRepository gradeRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Override
    public List<Grade> getAllGrades() {
        return gradeRepository.findAll();
    }

    @Override
    public Grade getGradeById(Long id) {
        return gradeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Grade not found with id: " + id));
    }

    @Override
    public List<Grade> getGradesByStudentId(Long studentId) {
        if (!studentRepository.existsById(studentId)) {
            throw new ResourceNotFoundException("Student not found with id: " + studentId);
        }
        return gradeRepository.findByStudentStudentId(studentId);
    }

    @Override
    public List<Grade> getGradesByCourseId(Long courseId) {
        if (!courseRepository.existsById(courseId)) {
            throw new ResourceNotFoundException("Course not found with id: " + courseId);
        }
        return gradeRepository.findByCourseCourseId(courseId);
    }

    @Override
    public Grade saveGrade(Grade grade) {
        if (grade.getStudent() == null || grade.getStudent().getStudentId() == null) {
            throw new IllegalArgumentException("Student must be specified");
        }
        if (grade.getCourse() == null || grade.getCourse().getCourseId() == null) {
            throw new IllegalArgumentException("Course must be specified");
        }

        Student student = studentRepository.findById(grade.getStudent().getStudentId())
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + grade.getStudent().getStudentId()));
        grade.setStudent(student);

        Course course = courseRepository.findById(grade.getCourse().getCourseId())
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + grade.getCourse().getCourseId()));
        grade.setCourse(course);

        return gradeRepository.save(grade);
    }

    @Override
    public Grade updateGrade(Long id, Grade grade) {
        Grade existing = getGradeById(id);

        if (grade.getStudent() != null && grade.getStudent().getStudentId() != null) {
            Student student = studentRepository.findById(grade.getStudent().getStudentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + grade.getStudent().getStudentId()));
            existing.setStudent(student);
        }

        if (grade.getCourse() != null && grade.getCourse().getCourseId() != null) {
            Course course = courseRepository.findById(grade.getCourse().getCourseId())
                    .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + grade.getCourse().getCourseId()));
            existing.setCourse(course);
        }

        existing.setGrade(grade.getGrade());
        existing.setScore(grade.getScore());

        return gradeRepository.save(existing);
    }

    @Override
    public void deleteGrade(Long id) {
        Grade existing = getGradeById(id);
        gradeRepository.delete(existing);
    }
}
