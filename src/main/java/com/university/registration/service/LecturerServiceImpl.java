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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Collections;

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
            return lecturerRepository.findAll();
        } else if (user.getRole() == Role.LECTURER) {
            return lecturerRepository.findByUserEmail(email)
                    .map(Collections::singletonList)
                    .orElse(Collections.emptyList());
        }
        return Collections.emptyList();
    }

    @Override
    public Lecturer getLecturerById(Long id) {
        Lecturer lecturer = lecturerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lecturer not found with id: " + id));

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            String email = auth.getName();
            User user = userRepository.findByEmail(email).orElse(null);
            if (user != null) {
                if (user.getRole() == Role.LECTURER) {
                    if (lecturer.getUser() == null || !lecturer.getUser().getEmail().equals(email)) {
                        throw new org.springframework.security.access.AccessDeniedException("You are not authorized to view this lecturer profile.");
                    }
                } else if (user.getRole() == Role.STUDENT) {
                    throw new org.springframework.security.access.AccessDeniedException("You are not authorized to view lecturer information.");
                }
            }
        }
        return lecturer;
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
            User existingUser = userRepository.findByUsername(user.getUsername())
                    .or(() -> userRepository.findByEmail(user.getEmail()))
                    .orElse(null);

            if (existingUser != null) {
                lecturer.setUser(existingUser);
            } else {
                user.setRole(Role.LECTURER);
                user.setPassword(passwordEncoder.encode(user.getPassword()));
            }
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
        if (lecturer.getUser() != null) {
            User updatedUser = lecturer.getUser();
            if (existing.getUser() != null) {
                User existingUser = existing.getUser();

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
            } else {
                User existingUser = userRepository.findByUsername(updatedUser.getUsername())
                        .or(() -> userRepository.findByEmail(updatedUser.getEmail()))
                        .orElse(null);

                if (existingUser != null) {
                    existing.setUser(existingUser);
                } else {
                    User newUser = new User();
                    newUser.setUsername(updatedUser.getUsername());
                    newUser.setEmail(updatedUser.getEmail());
                    newUser.setRole(Role.LECTURER);
                    newUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
                    existing.setUser(newUser);
                }
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
