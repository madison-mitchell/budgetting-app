package com.mmg.app.service;

import com.mmg.app.model.User;

import java.util.List;

public interface UserService {
    User saveUser(User user);
    User updateUser(User user);
    void deleteUser(Long id);
    User getUserById(Long id);
    User getUserByUsername(String username);
    List<User> getAllUsers();
}
