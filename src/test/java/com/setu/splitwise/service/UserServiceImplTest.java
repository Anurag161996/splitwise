package com.setu.splitwise.service;

import com.setu.splitwise.exceptions.UserNotFoundException;
import com.setu.splitwise.exceptions.UserValidationException;
import com.setu.splitwise.model.User;
import com.setu.splitwise.repository.UserRepository;
import com.setu.splitwise.service.impl.UserServiceImpl;
import com.setu.splitwise.validators.UserValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserValidator userValidator;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateUser() throws UserValidationException {
        User user = new User();
        user.setFirstName("Anurag");

        when(userRepository.save(any())).thenReturn(user);

        User createdUser = userService.createUser(user);

        assertEquals(user, createdUser);

        verify(userValidator, times(1)).validateNewUser(user);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testUpdateUser() throws UserValidationException, UserNotFoundException {
        Long userId = 1L;
        User updateUser = new User();
        updateUser.setFirstName("Anurag");

        User existingUser = new User();
        existingUser.setId(userId);
        existingUser.setFirstName("Raj");

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any())).thenReturn(existingUser);

        User updatedUser = userService.updateUser(userId, updateUser);

        assertEquals(updateUser.getFirstName(), updatedUser.getFirstName());

        verify(userValidator, times(1)).validateExistingUserInput(userId, updateUser);
        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).save(existingUser);
    }

    @Test
    void testUpdateUserNonExistingUser() throws UserValidationException {
        Long userId = 1L;
        User updateUser = new User();
        updateUser.setFirstName("Anurag");

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.updateUser(userId, updateUser));

        verify(userValidator, times(1)).validateExistingUserInput(userId, updateUser);
        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, never()).save(any());
    }


}
