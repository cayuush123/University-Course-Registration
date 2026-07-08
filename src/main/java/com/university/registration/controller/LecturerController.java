package com.university.registration.controller;

import com.university.registration.dto.*;
import com.university.registration.entity.Department;
import com.university.registration.entity.Lecturer;
import com.university.registration.entity.User;
import com.university.registration.service.LecturerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/lecturers")
@CrossOrigin(origins = "*")
public class LecturerController {

    @Autowired
    private LecturerService lecturerService;

    @GetMapping
    public List<LecturerResponseDTO> getAllLecturers() {
        return lecturerService.getAllLecturers().stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public LecturerResponseDTO getLecturerById(@PathVariable Long id) {
        return convertToResponseDTO(lecturerService.getLecturerById(id));
    }

    @PostMapping
    public LecturerResponseDTO createLecturer(@RequestBody LecturerRequestDTO request) {
        Lecturer lecturer = convertToEntity(request);
        Lecturer savedLecturer = lecturerService.saveLecturer(lecturer);
        return convertToResponseDTO(savedLecturer);
    }

    @PutMapping("/{id}")
    public LecturerResponseDTO updateLecturer(@PathVariable Long id, @RequestBody LecturerRequestDTO request) {
        Lecturer lecturer = convertToEntity(request);
        Lecturer updatedLecturer = lecturerService.updateLecturer(id, lecturer);
        return convertToResponseDTO(updatedLecturer);
    }

    @DeleteMapping("/{id}")
    public void deleteLecturer(@PathVariable Long id) {
        lecturerService.deleteLecturer(id);
    }

    private LecturerResponseDTO convertToResponseDTO(Lecturer lecturer) {
        if (lecturer == null) return null;

        DepartmentResponseDTO deptDTO = null;
        if (lecturer.getDepartment() != null) {
            Department d = lecturer.getDepartment();
            deptDTO = new DepartmentResponseDTO(d.getDepartmentId(), d.getDepartmentName(), d.getFaculty());
        }

        UserResponseDTO userDTO = null;
        if (lecturer.getUser() != null) {
            User u = lecturer.getUser();
            userDTO = new UserResponseDTO(u.getId(), u.getUsername(), u.getEmail(), u.getRole());
        }

        return new LecturerResponseDTO(
                lecturer.getLecturerId(),
                lecturer.getFirstName(),
                lecturer.getLastName(),
                lecturer.getGender(),
                lecturer.getRank(),
                lecturer.getPhoneNumber(),
                deptDTO,
                userDTO
        );
    }

    private Lecturer convertToEntity(LecturerRequestDTO dto) {
        if (dto == null) return null;

        Lecturer lecturer = new Lecturer();
        lecturer.setFirstName(dto.getFirstName());
        lecturer.setLastName(dto.getLastName());
        lecturer.setGender(dto.getGender());
        lecturer.setRank(dto.getRank());
        lecturer.setPhoneNumber(dto.getPhoneNumber());

        if (dto.getDepartmentId() != null) {
            Department dept = new Department();
            dept.setDepartmentId(dto.getDepartmentId());
            lecturer.setDepartment(dept);
        }

        if (dto.getUser() != null) {
            User user = new User();
            user.setUsername(dto.getUser().getUsername());
            user.setPassword(dto.getUser().getPassword());
            user.setEmail(dto.getUser().getEmail());
            user.setRole(dto.getUser().getRole());
            lecturer.setUser(user);
        }

        return lecturer;
    }
}
