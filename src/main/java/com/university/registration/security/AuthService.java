package com.university.registration.security;

import com.university.registration.dto.LoginRequestDTO;
import com.university.registration.dto.LoginResponseDTO;
import com.university.registration.dto.UserRequestDTO;
import com.university.registration.dto.UserResponseDTO;
import com.university.registration.entity.User;
import com.university.registration.entity.Student;
import com.university.registration.entity.Lecturer;
import com.university.registration.repository.UserRepository;
import com.university.registration.repository.StudentRepository;
import com.university.registration.repository.LecturerRepository;
import com.university.registration.enums.Role;
import com.university.registration.enums.Gender;
import com.university.registration.enums.LecturerRank;
import com.university.registration.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDate;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private LecturerRepository lecturerRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // LOGIN
    public LoginResponseDTO login(LoginRequestDTO request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() ->
                        new IllegalArgumentException("Invalid username or password"));

        String token = jwtService.generateToken(
                user.getEmail(),
                user.getRole().name()
        );

        return new LoginResponseDTO(
                token,
                user.getEmail(),
                user.getRole().name()
        );
    }

    // REGISTER
    public UserResponseDTO register(UserRequestDTO request) {

        User user = new User();

        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());

        user.setPassword(request.getPassword());

        user.setRole(request.getRole());

        User savedUser = userService.saveUser(user);

        if (savedUser.getRole() == Role.STUDENT) {
            Student student = new Student();
            student.setUser(savedUser);
            student.setFirstName(savedUser.getUsername());
            student.setLastName("");
            student.setGender(Gender.OTHER);
            student.setBirthDate(LocalDate.now());
            studentRepository.save(student);
        } else if (savedUser.getRole() == Role.LECTURER) {
            Lecturer lecturer = new Lecturer();
            lecturer.setUser(savedUser);
            lecturer.setFirstName(savedUser.getUsername());
            lecturer.setLastName("");
            lecturer.setGender(Gender.OTHER);
            lecturer.setRank(LecturerRank.LECTURER);
            lecturerRepository.save(lecturer);
        }

        return new UserResponseDTO(
                savedUser.getId(),
                savedUser.getUsername(),
                savedUser.getEmail(),
                savedUser.getRole()
        );
    }
}