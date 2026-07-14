package com.university.registration.service;

import com.university.registration.entity.Enrollment;
import java.util.List;

public interface EnrollmentService {
    List<Enrollment> getAllEnrollments();
    Enrollment getEnrollmentById(Long id);
    Enrollment saveEnrollment(Enrollment enrollment);
    Enrollment updateEnrollment(Long id, Enrollment enrollment);
    void deleteEnrollment(Long id);
}
