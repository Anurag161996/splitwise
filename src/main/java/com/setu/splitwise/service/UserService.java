package com.setu.splitwise.service;

import com.setu.splitwise.exceptions.UserNotFoundException;
import com.setu.splitwise.exceptions.UserValidationException;
import com.setu.splitwise.model.User;

import java.util.List;

public interface UserService {
    User createUser(User user) throws UserValidationException;

    User updateUser(Long userId, User user) throws UserValidationException, UserNotFoundException;

    void deleteUser(Long userId) throws UserValidationException;

    User getUserById(Long userId) throws UserValidationException, UserNotFoundException;

    List<User> getAllUsers();

    List<User> getAllUserById(List<Long> userIds);
}
