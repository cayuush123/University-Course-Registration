package com.university.registration.controller;

import com.university.registration.dto.*;
import com.university.registration.entity.Department;
import com.university.registration.entity.Student;
import com.university.registration.entity.User;
import com.university.registration.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/students")
@CrossOrigin(origins = "*")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @GetMapping
    public List<StudentResponseDTO> getAllStudents() {
        return studentService.getAllStudents().stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public StudentResponseDTO getStudentById(@PathVariable Long id) {
        return convertToResponseDTO(studentService.getStudentById(id));
    }

    @PostMapping
    public StudentResponseDTO createStudent(@RequestBody StudentRequestDTO request) {
        Student student = convertToEntity(request);
        Student savedStudent = studentService.saveStudent(student);
        return convertToResponseDTO(savedStudent);
    }

    @PutMapping("/{id}")
    public StudentResponseDTO updateStudent(@PathVariable Long id, @RequestBody StudentRequestDTO request) {
        Student student = convertToEntity(request);
        Student updatedStudent = studentService.updateStudent(id, student);
        return convertToResponseDTO(updatedStudent);
    }

    @DeleteMapping("/{id}")
    public void deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
    }

    private StudentResponseDTO convertToResponseDTO(Student student) {
        if (student == null) return null;

        DepartmentResponseDTO deptDTO = null;
        if (student.getDepartment() != null) {
            Department d = student.getDepartment();
            deptDTO = new DepartmentResponseDTO(d.getDepartmentId(), d.getDepartmentName(), d.getFaculty());
        }

        UserResponseDTO userDTO = null;
        if (student.getUser() != null) {
            User u = student.getUser();
            userDTO = new UserResponseDTO(u.getId(), u.getUsername(), u.getEmail(), u.getRole());
        }

        return new StudentResponseDTO(
                student.getStudentId(),
                student.getFirstName(),
                student.getLastName(),
                student.getGender(),
                student.getBirthDate(),
                student.getPhoneNumber(),
                deptDTO,
                userDTO
        );
    }

    private Student convertToEntity(StudentRequestDTO dto) {
        if (dto == null) return null;

        Student student = new Student();
        student.setFirstName(dto.getFirstName());
        student.setLastName(dto.getLastName());
        student.setGender(dto.getGender());
        student.setBirthDate(dto.getBirthDate());
        student.setPhoneNumber(dto.getPhoneNumber());

        if (dto.getDepartmentId() != null) {
            Department dept = new Department();
            dept.setDepartmentId(dto.getDepartmentId());
            student.setDepartment(dept);
        }

        if (dto.getUser() != null) {
            User user = new User();
            user.setUsername(dto.getUser().getUsername());
            user.setPassword(dto.getUser().getPassword());
            user.setEmail(dto.getUser().getEmail());
            user.setRole(dto.getUser().getRole());
            student.setUser(user);
        }

        return student;
    }
}
