package com.university.registration.service;

import com.university.registration.entity.Grade;
import com.university.registration.entity.Student;
import com.university.registration.entity.Course;
import com.university.registration.entity.User;
import com.university.registration.enums.Role;
import com.university.registration.exception.ResourceNotFoundException;
import com.university.registration.repository.GradeRepository;
import com.university.registration.repository.StudentRepository;
import com.university.registration.repository.CourseRepository;
import com.university.registration.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Collections;
import java.util.stream.Collectors;

@Service
public class GradeServiceImpl implements GradeService {

    @Autowired
    private GradeRepository gradeRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<Grade> getAllGrades() {
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
            return gradeRepository.findAll();
        } else if (user.getRole() == Role.LECTURER) {
            return gradeRepository.findGradesByLecturerEmail(email);
        } else if (user.getRole() == Role.STUDENT) {
            return gradeRepository.findGradesByStudentEmail(email);
        }
        return Collections.emptyList();
    }
    @Override
    public List<Grade> getMyGrades() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null) {
            return Collections.emptyList();
        }

        String email = auth.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        if (user.getRole() != Role.STUDENT) {
            throw new org.springframework.security.access.AccessDeniedException(
                    "Only students can view their own grades."
            );
        }

        return gradeRepository.findGradesByStudentEmail(email);
    }

    @Override
    public Grade getGradeById(Long id) {
        Grade grade = gradeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Grade not found with id: " + id));

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            String email = auth.getName();
            User user = userRepository.findByEmail(email).orElse(null);
            if (user != null) {
                if (user.getRole() == Role.STUDENT) {
                    if (grade.getStudent().getUser() == null || !grade.getStudent().getUser().getEmail().equals(email)) {
                        throw new org.springframework.security.access.AccessDeniedException("You are not authorized to view this grade.");
                    }
                } else if (user.getRole() == Role.LECTURER) {
                    if (grade.getCourse().getLecturer() == null || !grade.getCourse().getLecturer().getUser().getEmail().equals(email)) {
                        throw new org.springframework.security.access.AccessDeniedException("You are not authorized to view this grade.");
                    }
                }
            }
        }
        return grade;
    }

    @Override
    public List<Grade> getGradesByStudentId(Long studentId) {
        if (!studentRepository.existsById(studentId)) {
            throw new ResourceNotFoundException("Student not found with id: " + studentId);
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            String email = auth.getName();
            User user = userRepository.findByEmail(email).orElse(null);
            if (user != null) {
                if (user.getRole() == Role.STUDENT) {
                    Student loggedInStudent = studentRepository.findByUserEmail(email).orElse(null);
                    if (loggedInStudent == null || !loggedInStudent.getStudentId().equals(studentId)) {
                        throw new org.springframework.security.access.AccessDeniedException("You are not authorized to view these grades.");
                    }
                } else if (user.getRole() == Role.LECTURER) {
                    List<Grade> studentGrades = gradeRepository.findByStudentStudentId(studentId);
                    return studentGrades.stream()
                            .filter(g -> g.getCourse().getLecturer() != null && g.getCourse().getLecturer().getUser().getEmail().equals(email))
                            .collect(Collectors.toList());
                }
            }
        }
        return gradeRepository.findByStudentStudentId(studentId);
    }

    @Override
    public List<Grade> getGradesByCourseId(Long courseId) {
        if (!courseRepository.existsById(courseId)) {
            throw new ResourceNotFoundException("Course not found with id: " + courseId);
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            String email = auth.getName();
            User user = userRepository.findByEmail(email).orElse(null);
            if (user != null) {
                if (user.getRole() == Role.LECTURER) {
                    Course course = courseRepository.findById(courseId).orElse(null);
                    if (course == null || course.getLecturer() == null || !course.getLecturer().getUser().getEmail().equals(email)) {
                        throw new org.springframework.security.access.AccessDeniedException("You are not authorized to view grades for this course.");
                    }
                } else if (user.getRole() == Role.STUDENT) {
                    throw new org.springframework.security.access.AccessDeniedException("Students cannot view grades by course.");
                }
            }
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

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            String email = auth.getName();
            User user = userRepository.findByEmail(email).orElse(null);
            if (user != null) {
                if (user.getRole() == Role.LECTURER) {
                    Course course = courseRepository.findById(grade.getCourse().getCourseId())
                            .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + grade.getCourse().getCourseId()));
                    if (course.getLecturer() == null || !course.getLecturer().getUser().getEmail().equals(email)) {
                        throw new org.springframework.security.access.AccessDeniedException("You are not authorized to assign grades for this course.");
                    }
                } else if (user.getRole() == Role.STUDENT) {
                    throw new org.springframework.security.access.AccessDeniedException("Students cannot assign grades.");
                }
            }
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

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            String email = auth.getName();
            User user = userRepository.findByEmail(email).orElse(null);
            if (user != null) {
                if (user.getRole() == Role.LECTURER) {
                    if (existing.getCourse().getLecturer() == null || !existing.getCourse().getLecturer().getUser().getEmail().equals(email)) {
                        throw new org.springframework.security.access.AccessDeniedException("You are not authorized to update grades for this course.");
                    }
                } else if (user.getRole() == Role.STUDENT) {
                    throw new org.springframework.security.access.AccessDeniedException("Students cannot update grades.");
                }
            }
        }

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
        
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            String email = auth.getName();
            User user = userRepository.findByEmail(email).orElse(null);
            if (user != null && user.getRole() != Role.ADMIN) {
                throw new org.springframework.security.access.AccessDeniedException("Only administrators can delete grades.");
            }
        }

        gradeRepository.delete(existing);
    }
}
