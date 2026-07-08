package com.university.registration.service;

import com.university.registration.entity.Lecturer;
import com.university.registration.entity.Department;
import com.university.registration.entity.User;
import com.university.registration.enums.Role;
import com.university.registration.exception.ResourceNotFoundException;
import com.university.registration.repository.LecturerRepository;
import com.university.registration.repository.DepartmentRepository;
import com.university.registration.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class LecturerServiceImpl implements LecturerService {

    @Autowired
    private LecturerRepository lecturerRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public List<Lecturer> getAllLecturers() {
        return lecturerRepository.findAll();
    }

    @Override
    public Lecturer getLecturerById(Long id) {
        return lecturerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lecturer not found with id: " + id));
    }

    @Override
    public Lecturer saveLecturer(Lecturer lecturer) {
        // Resolve Department
        if (lecturer.getDepartment() != null && lecturer.getDepartment().getDepartmentId() != null) {
            Department dept = departmentRepository.findById(lecturer.getDepartment().getDepartmentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Department not found with id: " + lecturer.getDepartment().getDepartmentId()));
            lecturer.setDepartment(dept);
        }

        // Setup User credentials
        if (lecturer.getUser() != null) {
            User user = lecturer.getUser();
            if (userRepository.existsByUsername(user.getUsername())) {
                throw new IllegalArgumentException("Username already exists");
            }
            if (userRepository.existsByEmail(user.getEmail())) {
                throw new IllegalArgumentException("Email already exists");
            }
            user.setRole(Role.LECTURER);
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        return lecturerRepository.save(lecturer);
    }

    @Override
    public Lecturer updateLecturer(Long id, Lecturer lecturer) {
        Lecturer existing = getLecturerById(id);

        existing.setFirstName(lecturer.getFirstName());
        existing.setLastName(lecturer.getLastName());
        existing.setGender(lecturer.getGender());
        existing.setRank(lecturer.getRank());
        existing.setPhoneNumber(lecturer.getPhoneNumber());

        // Update Department
        if (lecturer.getDepartment() != null && lecturer.getDepartment().getDepartmentId() != null) {
            Department dept = departmentRepository.findById(lecturer.getDepartment().getDepartmentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Department not found with id: " + lecturer.getDepartment().getDepartmentId()));
            existing.setDepartment(dept);
        } else {
            existing.setDepartment(null);
        }

        // Update User
        if (lecturer.getUser() != null && existing.getUser() != null) {
            User existingUser = existing.getUser();
            User updatedUser = lecturer.getUser();

            if (!existingUser.getUsername().equals(updatedUser.getUsername()) && userRepository.existsByUsername(updatedUser.getUsername())) {
                throw new IllegalArgumentException("Username already exists");
            }
            if (!existingUser.getEmail().equals(updatedUser.getEmail()) && userRepository.existsByEmail(updatedUser.getEmail())) {
                throw new IllegalArgumentException("Email already exists");
            }

            existingUser.setUsername(updatedUser.getUsername());
            existingUser.setEmail(updatedUser.getEmail());
            
            if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
                existingUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
            }
        }

        return lecturerRepository.save(existing);
    }

    @Override
    public void deleteLecturer(Long id) {
        Lecturer existing = getLecturerById(id);
        lecturerRepository.delete(existing);
    }
}
