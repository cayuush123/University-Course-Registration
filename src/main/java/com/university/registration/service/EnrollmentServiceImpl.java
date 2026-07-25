package com.university.registration.service;

import com.university.registration.entity.Enrollment;
import com.university.registration.entity.Student;
import com.university.registration.entity.Course;
import com.university.registration.entity.User;
import com.university.registration.enums.Role;
import com.university.registration.exception.ResourceNotFoundException;
import com.university.registration.repository.EnrollmentRepository;
import com.university.registration.repository.StudentRepository;
import com.university.registration.repository.CourseRepository;
import com.university.registration.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Collections;

@Service
public class EnrollmentServiceImpl implements EnrollmentService {

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<Enrollment> getAllEnrollments() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            return Collections.emptyList();
        }
        String email = auth.getName();
        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) {
            return Collections.emptyList();
        }

        if (user.getRole() == Role.ADMIN) {
            return enrollmentRepository.findAll();
        } else if (user.getRole() == Role.LECTURER) {
            return enrollmentRepository.findEnrollmentsByLecturerEmail(email);
        } else if (user.getRole() == Role.STUDENT) {
            return enrollmentRepository.findEnrollmentsByStudentEmail(email);
        }
        return Collections.emptyList();
    }

    @Override
    public Enrollment getEnrollmentById(Long id) {
        Enrollment enrollment = enrollmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Enrollment not found with id: " + id));

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            String email = auth.getName();
            User user = userRepository.findByEmail(email).orElse(null);
            if (user != null) {
                if (user.getRole() == Role.STUDENT) {
                    if (enrollment.getStudent().getUser() == null || !enrollment.getStudent().getUser().getEmail().equals(email)) {
                        throw new org.springframework.security.access.AccessDeniedException("You are not authorized to view this enrollment.");
                    }
                } else if (user.getRole() == Role.LECTURER) {
                    if (enrollment.getCourse().getLecturer() == null || !enrollment.getCourse().getLecturer().getUser().getEmail().equals(email)) {
                        throw new org.springframework.security.access.AccessDeniedException("You are not authorized to view this enrollment.");
                    }
                }
            }
        }
        return enrollment;
    }

    @Override
    public Enrollment saveEnrollment(Enrollment enrollment) {
        if (enrollment.getStudent() == null || enrollment.getStudent().getStudentId() == null) {
            throw new IllegalArgumentException("Student must be specified");
        }
        if (enrollment.getCourse() == null || enrollment.getCourse().getCourseId() == null) {
            throw new IllegalArgumentException("Course must be specified");
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            String email = auth.getName();
            User user = userRepository.findByEmail(email).orElse(null);
            if (user != null) {
                if (user.getRole() == Role.STUDENT) {
                    Student s = studentRepository.findByUserEmail(email).orElse(null);
                    if (s == null || !s.getStudentId().equals(enrollment.getStudent().getStudentId())) {
                        throw new org.springframework.security.access.AccessDeniedException("You can only create enrollments for yourself.");
                    }
                } else if (user.getRole() == Role.LECTURER) {
                    throw new org.springframework.security.access.AccessDeniedException("Lecturers cannot manage enrollments.");
                }
            }
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
        Enrollment existing = getEnrollmentById(id); // getEnrollmentById already checks ownership!

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            String email = auth.getName();
            User user = userRepository.findByEmail(email).orElse(null);
            if (user != null) {
                if (user.getRole() == Role.STUDENT) {
                    if (enrollment.getStudent() != null && enrollment.getStudent().getStudentId() != null) {
                        Student s = studentRepository.findByUserEmail(email).orElse(null);
                        if (s == null || !s.getStudentId().equals(enrollment.getStudent().getStudentId())) {
                            throw new org.springframework.security.access.AccessDeniedException("You cannot change the student associated with this enrollment.");
                        }
                    }
                } else if (user.getRole() == Role.LECTURER) {
                    throw new org.springframework.security.access.AccessDeniedException("Lecturers cannot manage enrollments.");
                }
            }
        }

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
        Enrollment existing = getEnrollmentById(id); // getEnrollmentById already checks ownership!

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            String email = auth.getName();
            User user = userRepository.findByEmail(email).orElse(null);
            if (user != null && user.getRole() == Role.LECTURER) {
                throw new org.springframework.security.access.AccessDeniedException("Lecturers cannot manage enrollments.");
            }
        }

        enrollmentRepository.delete(existing);
    }
    @Override
    public List<Enrollment> getMyEnrollments() {
        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        return enrollmentRepository.findEnrollmentsByStudentEmail(email);
    }

    @Override
    public List<Enrollment> getEnrollmentsByCourse(Long courseId) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null) {
            return Collections.emptyList();
        }

        String email = auth.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        if (!courseRepository.existsById(courseId)) {
            throw new ResourceNotFoundException(
                    "Course not found with id: " + courseId);
        }

        List<Enrollment> enrollments = enrollmentRepository.findByCourseId(courseId);

        if (user.getRole() == Role.ADMIN) {
            return enrollments;
        }

        if (user.getRole() == Role.LECTURER) {

            Course course = courseRepository.findById(courseId)
                    .orElseThrow(() ->
                            new ResourceNotFoundException("Course not found"));

            if (course.getLecturer() == null
                    || course.getLecturer().getUser() == null
                    || !course.getLecturer().getUser().getEmail().equals(email)) {

                throw new org.springframework.security.access.AccessDeniedException(
                        "You are not authorized to view enrollments for this course.");
            }

            return enrollments;
        }

        throw new org.springframework.security.access.AccessDeniedException(
                "Students cannot view enrollments by course.");
    }
}
