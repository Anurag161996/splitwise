package com.setu.splitwise.service.impl;

import com.setu.splitwise.exceptions.UserNotFoundException;
import com.setu.splitwise.exceptions.UserValidationException;
import com.setu.splitwise.model.User;
import com.setu.splitwise.repository.UserRepository;
import com.setu.splitwise.service.UserService;
import com.setu.splitwise.utils.StringUtils;
import com.setu.splitwise.validators.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserValidator userValidator;

    @Override
    public User createUser(User user) throws UserValidationException {
        userValidator.validateNewUser(user);
        user.setCreatedAt(System.currentTimeMillis());
        user.setUpdatedAt(System.currentTimeMillis());
        return userRepository.save(user);
    }

    @Override
    public User updateUser(Long userId, User user) throws UserValidationException, UserNotFoundException {

        userValidator.validateExistingUserInput(userId, user);

        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));

        if (StringUtils.isNotNullOrEmpty(user.getBio())) {
            existingUser.setBio(user.getBio());
        }

        if (StringUtils.isNotNullOrEmpty(user.getFirstName())) {
            existingUser.setFirstName(user.getFirstName());
        }

        if (StringUtils.isNotNullOrEmpty(user.getLastName())) {
            existingUser.setLastName(user.getLastName());
        }

        if (StringUtils.isNotNullOrEmpty(user.getEmailId())) {
            if (!user.getEmailId().equals(existingUser.getEmailId()) && userRepository.existsByEmailId(user.getEmailId())) {
                throw new IllegalArgumentException("Email ID already exists.");
            }
            existingUser.setEmailId(user.getEmailId());
        }

        if (StringUtils.isNotNullOrEmpty(user.getPhoneNumber())) {
            if (!user.getPhoneNumber().equals(existingUser.getPhoneNumber()) && userRepository.existsByPhoneNumber(user.getPhoneNumber())) {
                throw new IllegalArgumentException("Phone number already exists.");
            }
            existingUser.setPhoneNumber(user.getPhoneNumber());
        }

        existingUser.setUpdatedAt(System.currentTimeMillis());
        return userRepository.save(existingUser);
    }

    @Override
    public void deleteUser(Long userId) throws UserValidationException {
        userValidator.validateID(userId);
        userRepository.deleteById(userId);
    }

    @Override
    public User getUserById(Long userId) throws UserValidationException, UserNotFoundException {
        userValidator.validateID(userId);
        return userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public List<User> getAllUserById(List<Long> userIds) {
        return userRepository.findAllById(userIds);
    }
}
