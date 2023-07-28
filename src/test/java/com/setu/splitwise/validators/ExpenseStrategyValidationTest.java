package com.setu.splitwise.validators;

import com.setu.splitwise.constants.ExpenseType;
import com.setu.splitwise.exceptions.ExpenseValidationException;
import com.setu.splitwise.exceptions.GroupNotFoundException;
import com.setu.splitwise.exceptions.GroupValidationException;
import com.setu.splitwise.exceptions.UserNotFoundException;
import com.setu.splitwise.exceptions.UserValidationException;
import com.setu.splitwise.model.UserContribution;
import com.setu.splitwise.model.UserGroup;
import com.setu.splitwise.model.input.ExpenseInput;
import com.setu.splitwise.repository.GroupRepository;
import com.setu.splitwise.repository.UserGroupRepository;
import com.setu.splitwise.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

class ExpenseStrategyValidationTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private GroupRepository groupRepository;

    @Mock
    private UserGroupRepository userGroupRepository;

    @InjectMocks
    private ExpenseStrategyValidation expenseStrategyValidation;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testValidateExactExpenseStrategy_ValidInput() throws ExpenseValidationException, UserValidationException, GroupValidationException, UserNotFoundException, GroupNotFoundException {
        ExpenseInput expenseInput = new ExpenseInput();
        expenseInput.setUserId(1L);
        expenseInput.setTransactionAmount(1000.0);
        expenseInput.setGroupId(2L);
        expenseInput.setExpenseType(ExpenseType.EXACT);
        expenseInput.setExpenseName("Expense1");
        expenseInput.setDescription("Expense description");
        expenseInput.setExpenseDate("28/07/2023");
        List<UserGroup> userGroupList = new ArrayList<>();

        UserGroup userGroup1 = new UserGroup();
        userGroup1.setGroupId(1L);
        userGroup1.setUserId(1L);
        userGroup1.setCreatedAt(System.currentTimeMillis());
        userGroupList.add(userGroup1);

        UserGroup userGroup2 = new UserGroup();
        userGroup2.setGroupId(2L);
        userGroup2.setUserId(2L);
        userGroup2.setCreatedAt(System.currentTimeMillis());
        userGroupList.add(userGroup2);

        List<UserContribution> userContributions = new ArrayList<>();
        userContributions.add(new UserContribution(1L, 0.5));
        userContributions.add(new UserContribution(2L, 0.5));
        expenseInput.setUserContributions(userContributions);

        when(userGroupRepository.findByGroupId(2L)).thenReturn(userGroupList);
        when(userRepository.existsById(1L)).thenReturn(true);
        when(userRepository.existsById(2L)).thenReturn(true);

        when(groupRepository.existsById(2L)).thenReturn(true);

        expenseStrategyValidation.validateExactExpenseStrategy(expenseInput);
    }


    @Test
    void testValidateExactExpenseStrategy_InvalidUserContributions() {
        ExpenseInput expenseInput = new ExpenseInput();
        expenseInput.setUserId(1L);
        expenseInput.setTransactionAmount(1000.0);
        expenseInput.setGroupId(2L);
        expenseInput.setExpenseType(ExpenseType.EXACT);
        expenseInput.setExpenseName("Expense1");
        expenseInput.setDescription("Expense description");
        expenseInput.setExpenseDate("28/07/2023");
        List<UserGroup> userGroupList = new ArrayList<>();

        UserGroup userGroup1 = new UserGroup();
        userGroup1.setGroupId(1L);
        userGroup1.setUserId(1L);
        userGroup1.setCreatedAt(System.currentTimeMillis());
        userGroupList.add(userGroup1);

        UserGroup userGroup2 = new UserGroup();
        userGroup2.setGroupId(2L);
        userGroup2.setUserId(2L);
        userGroup2.setCreatedAt(System.currentTimeMillis());
        userGroupList.add(userGroup2);

        List<UserContribution> userContributions = new ArrayList<>();
        userContributions.add(new UserContribution(1L, 0.6));
        userContributions.add(new UserContribution(2L, 0.3));
        expenseInput.setUserContributions(userContributions);

        when(userRepository.existsById(1L)).thenReturn(true);
        when(userRepository.existsById(2L)).thenReturn(true);

        when(groupRepository.existsById(2L)).thenReturn(true);
        when(userGroupRepository.findByGroupId(2L)).thenReturn(userGroupList);

        assertThrows(ExpenseValidationException.class, () -> expenseStrategyValidation.validateExactExpenseStrategy(expenseInput));
    }


}
