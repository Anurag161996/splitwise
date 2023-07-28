package com.setu.splitwise.validators;


import com.setu.splitwise.constants.ExpenseType;
import com.setu.splitwise.exceptions.ExpenseValidationException;
import com.setu.splitwise.exceptions.GroupNotFoundException;
import com.setu.splitwise.exceptions.GroupValidationException;
import com.setu.splitwise.exceptions.UserNotFoundException;
import com.setu.splitwise.exceptions.UserValidationException;
import com.setu.splitwise.model.UserContribution;
import com.setu.splitwise.model.UserGroup;
import com.setu.splitwise.model.input.ExpenseDateFilterInput;
import com.setu.splitwise.model.input.ExpenseInput;
import com.setu.splitwise.model.input.UserGroupFilterInput;
import com.setu.splitwise.repository.GroupRepository;
import com.setu.splitwise.repository.UserGroupRepository;
import com.setu.splitwise.repository.UserRepository;
import com.setu.splitwise.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class ExpenseStrategyValidation {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private UserGroupRepository userGroupRepository;

    private static final String DATE_FORMAT = "dd/MM/yyyy";

    public void validateExactExpenseStrategy(ExpenseInput expenseInput) throws ExpenseValidationException, UserValidationException, GroupValidationException, UserNotFoundException, GroupNotFoundException {
        validateUserId(expenseInput.getUserId());
        validateTransactionAmount(expenseInput.getTransactionAmount());
        validateGroupId(expenseInput.getGroupId());
        validateExpenseType(expenseInput.getExpenseType());
        validateExpenseName(expenseInput.getExpenseName());
        validateDescription(expenseInput.getDescription());
        validateExpenseDate(expenseInput.getExpenseDate());
        validateUserContributions(expenseInput.getUserContributions(), expenseInput.getGroupId());
        validateUserBelongsToGroup(expenseInput.getGroupId(), expenseInput.getUserId());
    }

    public void validateEqualExpenseStrategy(ExpenseInput expenseInput) throws ExpenseValidationException, UserValidationException, GroupValidationException, GroupNotFoundException, UserNotFoundException {
        validateUserId(expenseInput.getUserId());
        validateTransactionAmount(expenseInput.getTransactionAmount());
        validateGroupId(expenseInput.getGroupId());
        validateExpenseType(expenseInput.getExpenseType());
        validateExpenseName(expenseInput.getExpenseName());
        validateExpenseDate(expenseInput.getExpenseDate());
        validateDescription(expenseInput.getDescription());
        validateUserBelongsToGroup(expenseInput.getGroupId(), expenseInput.getUserId());
    }

    private void validateUserBelongsToGroup(Long groupId, Long userId) throws UserValidationException {

        List<UserGroup> userGroupList = userGroupRepository.findByGroupId(groupId);
        Set<Long> userIds = userGroupList.stream().map(item -> item.getUserId()).collect(Collectors.toSet());
        if(!userIds.contains(userId))
            throw new UserValidationException("user is not part of that group userId => " + userId);
    }

    public void validateExpenseFilterInput(Long userId, ExpenseDateFilterInput input) throws UserValidationException, ExpenseValidationException, UserNotFoundException {
        validateDate(input.getFromDate(), "fromDate");
        validateDate(input.getToDate(), "toDate");
        validateUserId(userId);
        if (input.getFromDate() != null && input.getToDate() != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
            LocalDate fromDate = LocalDate.parse(input.getFromDate(), formatter);
            LocalDate toDate = LocalDate.parse(input.getToDate(), formatter);

            if (fromDate.isAfter(toDate)) {
                throw new ExpenseValidationException("fromDate should be less than or equal to toDate.");
            }
        }
    }

    public void validateGroupFilterInput(Long userId, UserGroupFilterInput input) throws UserValidationException, GroupValidationException, UserNotFoundException, GroupNotFoundException {
        validateGroupId(input.getGroupId());
        validateUserId(userId);
    }

    private void validateDate(String dateStr, String fieldName) throws ExpenseValidationException {
        if(StringUtils.isNullOrEmpty(dateStr))
            throw new ExpenseValidationException(fieldName + " should be not be null");
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
            LocalDate.parse(dateStr, formatter);
        } catch (Exception e) {
            throw new ExpenseValidationException(fieldName + " should be a valid date in the format DD/MM/YYYY.");
        }
    }

    private void validateUserId(Long userId) throws UserValidationException, UserNotFoundException {
        if (userId == null) {
            throw new UserValidationException("UserId should not be null");
        }

        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException("UserId does not exist in the user table");
        }

    }

    private void validateTransactionAmount(Double transactionAmount) throws ExpenseValidationException {
        if (transactionAmount == null || transactionAmount <= 0 || transactionAmount > 100000) {
            throw new ExpenseValidationException("TransactionAmount should be positive and not greater than 1,00,000");
        }
    }

    private void validateExpenseDate(String expenseDate) throws ExpenseValidationException {
        if (StringUtils.isNullOrEmpty(expenseDate)) {
            throw new ExpenseValidationException("expenseDate should not be null");
        }
        validateDate(expenseDate, "expenseDate");
    }
    private void validateGroupId(Long groupId) throws GroupValidationException, GroupNotFoundException {
        if (groupId == null) {
            throw new GroupValidationException("GroupId should not be null");
        }

        if (!groupRepository.existsById(groupId)) {
            throw new GroupNotFoundException("GroupId does not exist in the group table");
        }
    }

    private void validateExpenseType(ExpenseType expenseType) throws ExpenseValidationException {
        if (expenseType == null) {
            throw new ExpenseValidationException("ExpenseType should not be null");
        }
    }

    private void validateExpenseName(String expenseName) throws ExpenseValidationException {
        if (StringUtils.isNullOrEmpty(expenseName)) {
            throw new ExpenseValidationException("ExpenseName should not be null or empty");
        }
    }

    private void validateDescription(String description) throws ExpenseValidationException {
        if (StringUtils.isNullOrEmpty(description)) {
            throw new ExpenseValidationException("Description should not be null or empty");
        }
    }

    private void validateUserContributions(List<UserContribution> userContributions, long groupId) throws ExpenseValidationException, UserValidationException, UserNotFoundException {
        if (userContributions == null || userContributions.isEmpty()) {
            throw new ExpenseValidationException("UserContributions should not be empty");
        }

        double totalContribution = 0.0;

        List<UserGroup> userGroupList = userGroupRepository.findByGroupId(groupId);
        Set<Long> userIds = userGroupList.stream().map(item -> item.getUserId()).collect(Collectors.toSet());
        Set<Long> visitedUserIds = new HashSet<>();
        for (UserContribution contribution : userContributions) {
            Long userId = contribution.getUserId();

            if (userId == null || !userIds.contains(userId) || !userRepository.existsById(userId))
                throw new UserNotFoundException("Invalid userId in UserContributions");

            if(visitedUserIds.contains(userId))
                throw new UserValidationException("userId is duplicated in UserContributions" + userId);

            Double userContribution = contribution.getContribution();
            if (userContribution == null || userContribution <= 0 || userContribution > 1) {
                throw new ExpenseValidationException("Invalid contribution value in UserContributions");
            }
            visitedUserIds.add(userId);
            totalContribution += userContribution;
        }

        if (Math.abs(totalContribution - 1.0) > 0.001) {
            throw new ExpenseValidationException("Sum of contributions in UserContributions should be equal to 1");
        }
    }
}