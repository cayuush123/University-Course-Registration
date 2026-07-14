package com.university.registration.service;

import com.university.registration.entity.Enrollment;
import com.university.registration.entity.Student;
import com.university.registration.entity.Course;
import com.university.registration.exception.ResourceNotFoundException;
import com.university.registration.repository.EnrollmentRepository;
import com.university.registration.repository.StudentRepository;
import com.university.registration.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class EnrollmentServiceImpl implements EnrollmentService {

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Override
    public List<Enrollment> getAllEnrollments() {
        return enrollmentRepository.findAll();
    }

    @Override
    public Enrollment getEnrollmentById(Long id) {
        return enrollmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Enrollment not found with id: " + id));
    }

    @Override
    public Enrollment saveEnrollment(Enrollment enrollment) {
        if (enrollment.getStudent() == null || enrollment.getStudent().getStudentId() == null) {
            throw new IllegalArgumentException("Student must be specified");
        }
        if (enrollment.getCourse() == null || enrollment.getCourse().getCourseId() == null) {
            throw new IllegalArgumentException("Course must be specified");
        }

        Student student = studentRepository.findById(enrollment.getStudent().getStudentId())
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + enrollment.getStudent().getStudentId()));
        enrollment.setStudent(student);

        Course course = courseRepository.findById(enrollment.getCourse().getCourseId())
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + enrollment.getCourse().getCourseId()));
        enrollment.setCourse(course);

        // Check for duplicate enrollment
        if (enrollmentRepository.existsByStudentStudentIdAndCourseCourseId(student.getStudentId(), course.getCourseId())) {
            throw new IllegalArgumentException("Student is already enrolled in this course");
        }

        return enrollmentRepository.save(enrollment);
    }

    @Override
    public Enrollment updateEnrollment(Long id, Enrollment enrollment) {
        Enrollment existing = getEnrollmentById(id);

        if (enrollment.getStudent() != null && enrollment.getStudent().getStudentId() != null) {
            Student student = studentRepository.findById(enrollment.getStudent().getStudentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + enrollment.getStudent().getStudentId()));
            
            // If student or course changed, check duplicate
            if (!student.getStudentId().equals(existing.getStudent().getStudentId()) || 
                (enrollment.getCourse() != null && enrollment.getCourse().getCourseId() != null && !enrollment.getCourse().getCourseId().equals(existing.getCourse().getCourseId()))) {
                Long targetCourseId = (enrollment.getCourse() != null && enrollment.getCourse().getCourseId() != null) ? enrollment.getCourse().getCourseId() : existing.getCourse().getCourseId();
                if (enrollmentRepository.existsByStudentStudentIdAndCourseCourseId(student.getStudentId(), targetCourseId)) {
                    throw new IllegalArgumentException("Student is already enrolled in this course");
                }
            }
            existing.setStudent(student);
        }

        if (enrollment.getCourse() != null && enrollment.getCourse().getCourseId() != null) {
            Course course = courseRepository.findById(enrollment.getCourse().getCourseId())
                    .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + enrollment.getCourse().getCourseId()));
            
            // If course changed but student did not (and wasn't handled in block above)
            if (!course.getCourseId().equals(existing.getCourse().getCourseId()) && 
                (enrollment.getStudent() == null || enrollment.getStudent().getStudentId() == null || enrollment.getStudent().getStudentId().equals(existing.getStudent().getStudentId()))) {
                if (enrollmentRepository.existsByStudentStudentIdAndCourseCourseId(existing.getStudent().getStudentId(), course.getCourseId())) {
                    throw new IllegalArgumentException("Student is already enrolled in this course");
                }
            }
            existing.setCourse(course);
        }

        existing.setEnrollmentDate(enrollment.getEnrollmentDate());
        existing.setStatus(enrollment.getStatus());

        return enrollmentRepository.save(existing);
    }

    @Override
    public void deleteEnrollment(Long id) {
        Enrollment existing = getEnrollmentById(id);
        enrollmentRepository.delete(existing);
    }
}
