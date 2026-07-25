package com.university.registration.controller;

import com.university.registration.dto.UserRequestDTO;
import com.university.registration.dto.UserResponseDTO;
import com.university.registration.entity.User;
import com.university.registration.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'LECTURER', 'STUDENT')")
    public List<UserResponseDTO> getAllUsers() {
        return userService.getAllUsers().stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/me")
    @PreAuthorize("hasAnyRole('ADMIN', 'LECTURER', 'STUDENT')")
    public UserResponseDTO getMe() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            throw new org.springframework.security.access.AccessDeniedException("Not authenticated");
        }
        String email = auth.getName();
        User user = userService.getUserByEmail(email);
        return convertToResponseDTO(user);
    }

    @PutMapping("/me")
    @PreAuthorize("hasAnyRole('ADMIN', 'LECTURER', 'STUDENT')")
    public UserResponseDTO updateMe(@RequestBody UserRequestDTO request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            throw new org.springframework.security.access.AccessDeniedException("Not authenticated");
        }
        String email = auth.getName();
        User user = userService.getUserByEmail(email);
        User updated = convertToEntity(request);
        User saved = userService.updateUser(user.getId(), updated);
        return convertToResponseDTO(saved);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'LECTURER', 'STUDENT')")
    public UserResponseDTO getUserById(@PathVariable Long id) {
        return convertToResponseDTO(userService.getUserById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public UserResponseDTO createUser(@RequestBody UserRequestDTO request) {
        User user = convertToEntity(request);
        User savedUser = userService.saveUser(user);
        return convertToResponseDTO(savedUser);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'LECTURER', 'STUDENT')")
    public UserResponseDTO updateUser(@PathVariable Long id, @RequestBody UserRequestDTO request) {
        User user = convertToEntity(request);
        User updatedUser = userService.updateUser(id, user);
        return convertToResponseDTO(updatedUser);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }

    private UserResponseDTO convertToResponseDTO(User user) {
        if (user == null) return null;

        return new UserResponseDTO(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole()
        );
    }

    private User convertToEntity(UserRequestDTO dto) {
        if (dto == null) return null;

        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(dto.getPassword());
        user.setEmail(dto.getEmail());
        user.setRole(dto.getRole());

        return user;
    }
}