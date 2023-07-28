package com.setu.splitwise.service;

import com.setu.splitwise.exceptions.ExpenseValidationException;
import com.setu.splitwise.exceptions.GroupNotFoundException;
import com.setu.splitwise.exceptions.GroupValidationException;
import com.setu.splitwise.exceptions.UserNotFoundException;
import com.setu.splitwise.exceptions.UserValidationException;
import com.setu.splitwise.model.Expense;
import com.setu.splitwise.model.UserContribution;
import com.setu.splitwise.model.input.ExpenseInput;

public interface ExpenseStrategy {
    Expense calculateExpense(ExpenseInput expenseInput) throws UserValidationException, GroupValidationException, ExpenseValidationException, UserNotFoundException, GroupNotFoundException;

    Double calculatePendingAmount(Expense expense, Long userId);
}
