package com.setu.splitwise.service.impl;

import com.setu.splitwise.constants.ExpenseType;
import com.setu.splitwise.exceptions.ExpenseValidationException;
import com.setu.splitwise.exceptions.GroupNotFoundException;
import com.setu.splitwise.exceptions.GroupValidationException;
import com.setu.splitwise.exceptions.UserNotFoundException;
import com.setu.splitwise.exceptions.UserValidationException;
import com.setu.splitwise.model.Expense;
import com.setu.splitwise.model.UserContribution;
import com.setu.splitwise.model.input.ExpenseInput;
import com.setu.splitwise.service.ExpenseStrategy;
import com.setu.splitwise.utils.DateUtils;
import com.setu.splitwise.validators.ExpenseStrategyValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExactExpenseServiceImpl implements ExpenseStrategy {
    @Autowired
    ExpenseStrategyValidation expenseStrategyValidation;

    @Override
    public Expense calculateExpense(ExpenseInput expenseInput) throws UserValidationException, GroupValidationException, ExpenseValidationException, UserNotFoundException, GroupNotFoundException {
        expenseStrategyValidation.validateExactExpenseStrategy(expenseInput);
        return mapExpenseInputToExpense(expenseInput);
    }

    @Override
    public Double calculatePendingAmount(Expense expense, Long userId) {
        Double transactionAmount = expense.getTransactionAmount();
        for (UserContribution userContribution : expense.getUserContributions()) {
            if(userContribution.getUserId() == userId) {
                if (userContribution.getUserId() == expense.getUserId())
                    transactionAmount = transactionAmount * (1 - userContribution.getContribution());
                else
                    transactionAmount = -transactionAmount * userContribution.getContribution();
                return transactionAmount;
            }
        }

        return 0d;
    }


    private Expense mapExpenseInputToExpense(ExpenseInput expenseInput) {

        Expense expense = new Expense();
        expense.setExpenseName(expenseInput.getExpenseName());
        expense.setExpenseType(ExpenseType.EXACT);
        expense.setTransactionAmount(expenseInput.getTransactionAmount());
        expense.setGroupId(expenseInput.getGroupId());
        expense.setUserId(expenseInput.getUserId());
        expense.setDescription(expenseInput.getDescription());
        expense.setExpenseAt(DateUtils.getStartOfTheDayEpoch(expenseInput.getExpenseDate()));
        expense.setCreatedAt(System.currentTimeMillis());
        expense.setUpdatedAt(System.currentTimeMillis());
        expense.setUserContributions(expenseInput.getUserContributions());
        return expense;

    }

}
