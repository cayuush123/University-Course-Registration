package com.university.registration.service;

import com.university.registration.entity.Course;
import java.util.List;

public interface CourseService {
    List<Course> getAllCourses();
    Course getCourseById(Long id);
    Course saveCourse(Course course);
    Course updateCourse(Long id, Course course);
    void deleteCourse(Long id);
}
