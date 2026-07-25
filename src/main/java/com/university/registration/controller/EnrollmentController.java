package com.university.registration.controller;

import com.university.registration.entity.Enrollment;
import com.university.registration.service.EnrollmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/enrollments")
@CrossOrigin(origins = "*")
public class EnrollmentController {

    @Autowired
    private EnrollmentService enrollmentService;

    @GetMapping("/my")
    @PreAuthorize("hasRole('STUDENT')")
    public List<Enrollment> getMyEnrollments() {
        return enrollmentService.getMyEnrollments();
    }

    @GetMapping
    public List<Enrollment> getAllEnrollments() {
        return enrollmentService.getAllEnrollments();
    }

    @GetMapping("/{id}")
    public Enrollment getEnrollmentById(@PathVariable Long id) {
        return enrollmentService.getEnrollmentById(id);
    }

    // ADD THIS METHOD
    @GetMapping("/course/{courseId}")
    @PreAuthorize("hasAnyRole('ADMIN','LECTURER')")
    public List<Enrollment> getEnrollmentsByCourse(@PathVariable Long courseId) {
        return enrollmentService.getEnrollmentsByCourse(courseId);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','STUDENT')")
    public Enrollment createEnrollment(@RequestBody Enrollment enrollment) {
        return enrollmentService.saveEnrollment(enrollment);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','STUDENT')")
    public Enrollment updateEnrollment(@PathVariable Long id,
                                       @RequestBody Enrollment enrollment) {
        return enrollmentService.updateEnrollment(id, enrollment);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteEnrollment(@PathVariable Long id) {
        enrollmentService.deleteEnrollment(id);
    }
}