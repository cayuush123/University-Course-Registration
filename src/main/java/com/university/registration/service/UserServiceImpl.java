package com.university.registration.service;

import com.university.registration.entity.User;
import com.university.registration.enums.Role;
import com.university.registration.exception.ResourceNotFoundException;
import com.university.registration.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Collections;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public List<User> getAllUsers() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            return Collections.emptyList();
        }
        String email = auth.getName();
        User loggedInUser = userRepository.findByEmail(email).orElse(null);
        if (loggedInUser == null) {
            return Collections.emptyList();
        }

        if (loggedInUser.getRole() == Role.ADMIN) {
            return userRepository.findAll();
        } else {
            return Collections.singletonList(loggedInUser);
        }
    }

    @Override
    public User getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            String email = auth.getName();
            User loggedInUser = userRepository.findByEmail(email).orElse(null);
            if (loggedInUser != null && loggedInUser.getRole() != Role.ADMIN) {
                if (!loggedInUser.getId().equals(id)) {
                    throw new org.springframework.security.access.AccessDeniedException("You are not authorized to view this user profile.");
                }
            }
        }
        return user;
    }

    @Override
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
    }

    @Override
    public User saveUser(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public User updateUser(Long id, User user) {
        User existing = getUserById(id);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            String email = auth.getName();
            User loggedInUser = userRepository.findByEmail(email).orElse(null);
            if (loggedInUser != null && loggedInUser.getRole() != Role.ADMIN) {
                if (!loggedInUser.getId().equals(id)) {
                    throw new org.springframework.security.access.AccessDeniedException("You are not authorized to update this user profile.");
                }
                // Force role and email to remain the same for non-admin to prevent privilege escalation
                user.setRole(loggedInUser.getRole());
                user.setEmail(loggedInUser.getEmail());
            }
        }
        
        if (!existing.getUsername().equals(user.getUsername()) && userRepository.existsByUsername(user.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }
        if (!existing.getEmail().equals(user.getEmail()) && userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }

        existing.setUsername(user.getUsername());
        existing.setEmail(user.getEmail());
        existing.setRole(user.getRole());
        
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            existing.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        
        return userRepository.save(existing);
    }

    @Override
    public void deleteUser(Long id) {
        User existing = getUserById(id);
        userRepository.delete(existing);
    }
}
