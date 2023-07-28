package com.setu.splitwise.service.impl;

import com.setu.splitwise.constants.ExpenseType;
import com.setu.splitwise.exceptions.ExpenseValidationException;
import com.setu.splitwise.exceptions.GroupNotFoundException;
import com.setu.splitwise.exceptions.GroupValidationException;
import com.setu.splitwise.exceptions.UserNotFoundException;
import com.setu.splitwise.exceptions.UserValidationException;
import com.setu.splitwise.model.Expense;
import com.setu.splitwise.model.UserGroup;
import com.setu.splitwise.model.input.ExpenseInput;
import com.setu.splitwise.service.ExpenseStrategy;
import com.setu.splitwise.service.UserGroupService;
import com.setu.splitwise.utils.DateUtils;
import com.setu.splitwise.validators.ExpenseStrategyValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EqualExpenseServiceImpl implements ExpenseStrategy {

    @Autowired
    ExpenseStrategyValidation expenseStrategyValidation;

    @Autowired
    UserGroupService userGroupService;

    @Override
    public Expense calculateExpense(ExpenseInput expenseInput) throws UserValidationException, GroupValidationException, ExpenseValidationException, UserNotFoundException, GroupNotFoundException {
        expenseStrategyValidation.validateEqualExpenseStrategy(expenseInput);
        return mapExpenseInputToExpense(expenseInput);
    }

    @Override
    public Double calculatePendingAmount(Expense expense, Long userId) {
        List<UserGroup> userGroupList = userGroupService.findUserGroupByGroupId(expense.getGroupId());
        Double transactionAmount = expense.getTransactionAmount();
        if (userId != expense.getUserId())
            return -transactionAmount / userGroupList.size();
        return (transactionAmount * (userGroupList.size() - 1)) / userGroupList.size();
    }

    private Expense mapExpenseInputToExpense(ExpenseInput expenseInput) {

        Expense expense = new Expense();
        expense.setExpenseName(expenseInput.getExpenseName());
        expense.setExpenseType(ExpenseType.EQUAL);
        expense.setTransactionAmount(expenseInput.getTransactionAmount());
        expense.setGroupId(expenseInput.getGroupId());
        expense.setUserId(expenseInput.getUserId());
        expense.setDescription(expenseInput.getDescription());
        expense.setExpenseAt(DateUtils.getStartOfTheDayEpoch(expenseInput.getExpenseDate()));
        expense.setCreatedAt(System.currentTimeMillis());
        expense.setUpdatedAt(System.currentTimeMillis());
        return expense;

    }

}
