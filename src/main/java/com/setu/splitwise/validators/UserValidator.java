package com.setu.splitwise.validators;
import com.setu.splitwise.exceptions.UserValidationException;
import com.setu.splitwise.model.User;
import com.setu.splitwise.repository.UserRepository;
import com.setu.splitwise.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserValidator {

    @Autowired
    private UserRepository userRepository;
    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
    private static final String PHONE_REGEX = "^(\\+91)[0-9]{10}$";

    public void validate(User user) throws UserValidationException {
        validateFirstName(user.getFirstName());
        validateLastName(user.getLastName());
        validateEmailId(user.getEmailId());
        validatePhoneNumber(user.getPhoneNumber());
    }

    public void validateExistingUserInput(Long userId, User user) throws UserValidationException {
        validateID(userId);
        if(StringUtils.isNotNullOrEmpty(user.getEmailId())) {
            if (!user.getEmailId().matches(EMAIL_REGEX)) {
                throw new UserValidationException("Invalid email ID format");
            }
        }
        if(StringUtils.isNotNullOrEmpty(user.getPhoneNumber())) {
            if (!user.getPhoneNumber().matches(PHONE_REGEX)) {
                throw new UserValidationException("Invalid Indian phone number format");
            }
        }
    }

    public void validateID(Long id) throws UserValidationException {
        if (id == null) {
            throw new UserValidationException("User ID cannot be null.");
        }
    }

    public void validateNewUser(User user) throws UserValidationException {
        validate(user);
        validateIdIsNull(user.getId());
        validateUniqueEmailId(user.getEmailId());
        validateUniquePhoneNumber(user.getPhoneNumber());
    }

    private void validateIdIsNull(Long id) throws UserValidationException {
        if (id != null) {
            throw new UserValidationException("ID should be empty for new user creation");
        }
    }

    private void validateUniqueEmailId(String emailId) throws UserValidationException {
        if (userRepository.existsByEmailId(emailId)) {
            throw new UserValidationException("Email ID already exists in the database");
        }
    }

    private void validateUniquePhoneNumber(String phoneNumber) throws UserValidationException {
        if (userRepository.existsByPhoneNumber(phoneNumber)) {
            throw new UserValidationException("Phone number already exists in the database");
        }
    }

    private void validateFirstName(String firstName) throws UserValidationException {
        if (StringUtils.isNullOrEmpty(firstName)) {
            throw new UserValidationException("First name cannot be null or empty");
        }
    }

    private void validateLastName(String lastName) throws UserValidationException {
        if (StringUtils.isNullOrEmpty(lastName)) {
            throw new UserValidationException("Last name cannot be null or empty");
        }
    }

    private void validateEmailId(String emailId) throws UserValidationException {
        if (StringUtils.isNullOrEmpty(emailId)) {
            throw new UserValidationException("Email ID cannot be null or empty");
        }
        if (!emailId.matches(EMAIL_REGEX)) {
            throw new UserValidationException("Invalid email ID format");
        }
    }

    private void validatePhoneNumber(String phoneNumber) throws UserValidationException {
        if (StringUtils.isNullOrEmpty(phoneNumber)) {
            throw new UserValidationException("Phone number cannot be null or empty");
        }
        if (!phoneNumber.matches(PHONE_REGEX)) {
            throw new UserValidationException("Invalid Indian phone number format");
        }
    }
}
