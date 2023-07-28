package com.setu.splitwise.validators;

import com.setu.splitwise.exceptions.UserValidationException;
import com.setu.splitwise.model.User;
import com.setu.splitwise.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserValidatorTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserValidator userValidator;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testValidate() throws UserValidationException {
        User user = new User();
        user.setFirstName("Anurag");
        user.setLastName("Gupta");
        user.setEmailId("anurag.gupta@example.com");
        user.setPhoneNumber("+918787878787");

        assertDoesNotThrow(() -> userValidator.validate(user));
    }

    @Test
    void testValidateFirstNameNull() {
        User user = new User();

        assertThrows(UserValidationException.class, () -> userValidator.validate(user));
    }

    @Test
    void testValidateLastNameEmpty() {
        User user = new User();
        user.setFirstName("Anurag");

        assertThrows(UserValidationException.class, () -> userValidator.validate(user));
    }

    @Test
    void testValidateEmailIdNull() {
        User user = new User();
        user.setFirstName("Anurag");
        user.setLastName("Gupta");

        assertThrows(UserValidationException.class, () -> userValidator.validate(user));
    }

    @Test
    void testValidatePhoneNumberNull() {
        User user = new User();
        user.setFirstName("Anurag");
        user.setLastName("Gupta");
        user.setEmailId("Anurag@example.com");

        assertThrows(UserValidationException.class, () -> userValidator.validate(user));
    }


    @Test
    void testValidateExistingUserInput() {
        Long userId = 1L;
        User user = new User();
        user.setEmailId("Anurag@example.com");
        user.setPhoneNumber("+919876543210");

        when(userRepository.existsByEmailId(user.getEmailId())).thenReturn(false);
        when(userRepository.existsByPhoneNumber(user.getPhoneNumber())).thenReturn(false);

        assertDoesNotThrow(() -> userValidator.validateExistingUserInput(userId, user));
    }

    @Test
    void testValidateExistingUserInputInvalidEmail() {
        Long userId = 1L;
        User user = new User();
        user.setEmailId("invalid-email");

        assertThrows(UserValidationException.class, () -> userValidator.validateExistingUserInput(userId, user));
    }

    @Test
    void testValidateExistingUserInputInvalidPhoneNumber() {
        Long userId = 1L;
        User user = new User();
        user.setPhoneNumber("invalid-phone");

        assertThrows(UserValidationException.class, () -> userValidator.validateExistingUserInput(userId, user));
    }

}
