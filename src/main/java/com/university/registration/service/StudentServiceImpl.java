package com.university.registration.service;

import com.university.registration.entity.Student;
import com.university.registration.entity.Department;
import com.university.registration.entity.User;
import com.university.registration.enums.Role;
import com.university.registration.exception.ResourceNotFoundException;
import com.university.registration.repository.StudentRepository;
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
public class StudentServiceImpl implements StudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public List<Student> getAllStudents() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        System.out.println("==================================");
        System.out.println("AUTH = " + auth);

        if (auth == null) {
            System.out.println("Authentication is NULL");
            return Collections.emptyList();
        }

        System.out.println("NAME = " + auth.getName());

        String email = auth.getName();

        User user = userRepository.findByEmail(email).orElse(null);

        System.out.println("USER = " + user);

        if (user == null) {
            System.out.println("User not found!");
            return Collections.emptyList();
        }

        System.out.println("ROLE = " + user.getRole());

        if (user.getRole() == Role.ADMIN) {
            System.out.println("Loading ALL students...");
            return studentRepository.findAll();

        } else if (user.getRole() == Role.LECTURER) {
            System.out.println("Loading lecturer students...");
            return studentRepository.findStudentsByLecturerEmail(user.getEmail());

        } else if (user.getRole() == Role.STUDENT) {
            System.out.println("Loading student profile...");
            return studentRepository.findByUserEmail(user.getEmail())
                    .map(Collections::singletonList)
                    .orElse(Collections.emptyList());
        }

        return Collections.emptyList();
    }

    @Override
    public Student getStudentById(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + id));

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            String email = auth.getName();

            User user = userRepository.findByEmail(email).orElse(null);
            if (user != null) {
                if (user.getRole() == Role.STUDENT) {
                    if (student.getUser() == null || !student.getUser().getEmail().equals(email)) {
                        throw new org.springframework.security.access.AccessDeniedException("You are not authorized to view this student profile.");
                    }
                } else if (user.getRole() == Role.LECTURER) {
                    List<Student> myStudents = studentRepository.findStudentsByLecturerEmail(email);
                    boolean isMyStudent = myStudents.stream().anyMatch(s -> s.getStudentId().equals(id));
                    if (!isMyStudent) {
                        throw new org.springframework.security.access.AccessDeniedException("You are not authorized to view this student profile.");
                    }
                }
            }
        }
        return student;
    }

    @Override
    public Student saveStudent(Student student) {
        // Resolve Department
        if (student.getDepartment() != null && student.getDepartment().getDepartmentId() != null) {
            Department dept = departmentRepository.findById(student.getDepartment().getDepartmentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Department not found with id: " + student.getDepartment().getDepartmentId()));
            student.setDepartment(dept);
        }

        // Setup User credentials
        if (student.getUser() != null) {
            User user = student.getUser();
            User existingUser = userRepository.findByUsername(user.getUsername())
                    .or(() -> userRepository.findByEmail(user.getEmail()))
                    .orElse(null);

            if (existingUser != null) {
                student.setUser(existingUser);
            } else {
                user.setRole(Role.STUDENT);
                user.setPassword(passwordEncoder.encode(user.getPassword()));
            }
        }

        return studentRepository.save(student);
    }

    @Override
    public Student updateStudent(Long id, Student student) {
        Student existing = getStudentById(id);

        existing.setFirstName(student.getFirstName());
        existing.setLastName(student.getLastName());
        existing.setGender(student.getGender());
        existing.setBirthDate(student.getBirthDate());
        existing.setPhoneNumber(student.getPhoneNumber());

        // Update Department
        if (student.getDepartment() != null && student.getDepartment().getDepartmentId() != null) {
            Department dept = departmentRepository.findById(student.getDepartment().getDepartmentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Department not found with id: " + student.getDepartment().getDepartmentId()));
            existing.setDepartment(dept);
        } else {
            existing.setDepartment(null);
        }

        // Update User
        if (student.getUser() != null) {
            User updatedUser = student.getUser();
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
                    newUser.setRole(Role.STUDENT);
                    newUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
                    existing.setUser(newUser);
                }
            }
        }

        return studentRepository.save(existing);
    }

    @Override
    public void deleteStudent(Long id) {
        Student existing = getStudentById(id);
        studentRepository.delete(existing);
    }
}
