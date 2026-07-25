package com.university.registration.service;

import com.university.registration.entity.Course;
import com.university.registration.entity.Department;
import com.university.registration.entity.Lecturer;
import com.university.registration.entity.User;
import com.university.registration.enums.Role;
import com.university.registration.exception.ResourceNotFoundException;
import com.university.registration.repository.CourseRepository;
import com.university.registration.repository.DepartmentRepository;
import com.university.registration.repository.LecturerRepository;
import com.university.registration.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Collections;

@Service
public class CourseServiceImpl implements CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private LecturerRepository lecturerRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<Course> getAllCourses() {
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
            return courseRepository.findAll();
        } else if (user.getRole() == Role.LECTURER) {
            return courseRepository.findByLecturerEmail(email);
        } else if (user.getRole() == Role.STUDENT) {
            return courseRepository.findEnrolledCoursesByStudentEmail(email);
        }
        return Collections.emptyList();
    }

    @Override
    public Course getCourseById(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + id));

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            String email = auth.getName();
            User user = userRepository.findByEmail(email).orElse(null);
            if (user != null) {
                if (user.getRole() == Role.STUDENT) {
                    List<Course> enrolled = courseRepository.findEnrolledCoursesByStudentEmail(email);
                    boolean isEnrolled = enrolled.stream().anyMatch(c -> c.getCourseId().equals(id));
                    if (!isEnrolled) {
                        throw new org.springframework.security.access.AccessDeniedException("You are not enrolled in this course.");
                    }
                } else if (user.getRole() == Role.LECTURER) {
                    if (course.getLecturer() == null || !course.getLecturer().getUser().getEmail().equals(email)) {
                        throw new org.springframework.security.access.AccessDeniedException("You are not assigned to teach this course.");
                    }
                }
            }
        }
        return course;
    }

    @Override
    public Course saveCourse(Course course) {
        if (course.getDepartment() != null && course.getDepartment().getDepartmentId() != null) {
            Department dept = departmentRepository.findById(course.getDepartment().getDepartmentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Department not found with id: " + course.getDepartment().getDepartmentId()));
            course.setDepartment(dept);
        }
        if (course.getLecturer() != null && course.getLecturer().getLecturerId() != null) {
            Lecturer lec = lecturerRepository.findById(course.getLecturer().getLecturerId())
                    .orElseThrow(() -> new ResourceNotFoundException("Lecturer not found with id: " + course.getLecturer().getLecturerId()));
            course.setLecturer(lec);
        }
        return courseRepository.save(course);
    }

    @Override
    public Course updateCourse(Long id, Course course) {
        Course existing = getCourseById(id);
        existing.setCourseCode(course.getCourseCode());
        existing.setCourseName(course.getCourseName());
        existing.setCredits(course.getCredits());

        if (course.getDepartment() != null && course.getDepartment().getDepartmentId() != null) {
            Department dept = departmentRepository.findById(course.getDepartment().getDepartmentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Department not found with id: " + course.getDepartment().getDepartmentId()));
            existing.setDepartment(dept);
        } else {
            existing.setDepartment(null);
        }

        if (course.getLecturer() != null && course.getLecturer().getLecturerId() != null) {
            Lecturer lec = lecturerRepository.findById(course.getLecturer().getLecturerId())
                    .orElseThrow(() -> new ResourceNotFoundException("Lecturer not found with id: " + course.getLecturer().getLecturerId()));
            existing.setLecturer(lec);
        } else {
            existing.setLecturer(null);
        }

        return courseRepository.save(existing);
    }

    @Override
    public void deleteCourse(Long id) {
        Course existing = getCourseById(id);
        courseRepository.delete(existing);
    }
}
