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
import org.springframework.stereotype.Service;
import java.util.List;

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
        return studentRepository.findAll();
    }

    @Override
    public Student getStudentById(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + id));
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
            if (userRepository.existsByUsername(user.getUsername())) {
                throw new IllegalArgumentException("Username already exists");
            }
            if (userRepository.existsByEmail(user.getEmail())) {
                throw new IllegalArgumentException("Email already exists");
            }
            user.setRole(Role.STUDENT);
            user.setPassword(passwordEncoder.encode(user.getPassword()));
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
        if (student.getUser() != null && existing.getUser() != null) {
            User existingUser = existing.getUser();
            User updatedUser = student.getUser();

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

        return studentRepository.save(existing);
    }

    @Override
    public void deleteStudent(Long id) {
        Student existing = getStudentById(id);
        studentRepository.delete(existing);
    }
}
