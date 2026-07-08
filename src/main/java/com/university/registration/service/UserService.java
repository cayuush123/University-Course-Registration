package com.university.registration.service;

import com.university.registration.entity.User;
import java.util.List;

public interface UserService {
    List<User> getAllUsers();
    User getUserById(Long id);
    User getUserByUsername(String username);
    User saveUser(User user);
    User updateUser(Long id, User user);
    void deleteUser(Long id);
}
