package com.setu.splitwise.service;

import com.setu.splitwise.exceptions.ExpenseValidationException;
import com.setu.splitwise.exceptions.GroupNotFoundException;
import com.setu.splitwise.exceptions.GroupValidationException;
import com.setu.splitwise.exceptions.UserNotFoundException;
import com.setu.splitwise.exceptions.UserValidationException;
import com.setu.splitwise.model.Expense;
import com.setu.splitwise.model.input.ExpenseDateFilterInput;
import com.setu.splitwise.model.input.ExpenseInput;
import com.setu.splitwise.model.input.UserGroupFilterInput;
import com.setu.splitwise.model.view.ExpenseView;

import java.util.List;

public interface ExpenseService {
    Expense addExpense(ExpenseInput expenseInput) throws UserValidationException, GroupValidationException, ExpenseValidationException, UserNotFoundException, GroupNotFoundException;

    List<ExpenseView> viewAllExpenseByGroupId(Long userId, UserGroupFilterInput input) throws GroupValidationException, UserValidationException, ExpenseValidationException, UserNotFoundException, GroupNotFoundException;

    List<ExpenseView> viewAllExpenseByUserId(Long userId, ExpenseDateFilterInput input) throws GroupValidationException, UserValidationException, ExpenseValidationException, UserNotFoundException;

    List<Expense> getAllExpenses();

    void deleteExpenses(Long id);
}
