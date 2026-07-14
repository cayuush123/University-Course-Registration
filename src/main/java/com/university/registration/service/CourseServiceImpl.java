package com.university.registration.service;

import com.university.registration.entity.Course;
import com.university.registration.entity.Department;
import com.university.registration.entity.Lecturer;
import com.university.registration.exception.ResourceNotFoundException;
import com.university.registration.repository.CourseRepository;
import com.university.registration.repository.DepartmentRepository;
import com.university.registration.repository.LecturerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CourseServiceImpl implements CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private LecturerRepository lecturerRepository;

    @Override
    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    @Override
    public Course getCourseById(Long id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + id));
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
