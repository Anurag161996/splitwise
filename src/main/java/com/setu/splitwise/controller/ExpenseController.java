package com.setu.splitwise.controller;

import com.setu.splitwise.constants.Urls;
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
import com.setu.splitwise.service.ExpenseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping(Urls.EXPENSE)
@Slf4j
public class ExpenseController {

    @Autowired
    private ExpenseService expenseService;

    @PostMapping
    public ResponseEntity<Expense> createExpense(@RequestBody ExpenseInput expenseInput) throws GroupValidationException, UserValidationException, ExpenseValidationException, UserNotFoundException, GroupNotFoundException {
        Expense expense = expenseService.addExpense(expenseInput);
        return ResponseEntity.ok(expense);
    }

    @GetMapping
    public ResponseEntity<List<Expense>> getAllExpense() {
        return ResponseEntity.ok(expenseService.getAllExpenses());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity getAllExpense(@PathVariable Long id) {
        expenseService.deleteExpenses(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/filterByDate/{id}")
    public ResponseEntity<List<ExpenseView>> filterExpenseByDate(@PathVariable Long id, @RequestBody ExpenseDateFilterInput expenseInput) throws GroupValidationException, UserValidationException, ExpenseValidationException, UserNotFoundException {
        List<ExpenseView> expenses = expenseService.viewAllExpenseByUserId(id, expenseInput);
        return ResponseEntity.ok(expenses);
    }

    @PostMapping("/filterByGroupId/{id}")
    public ResponseEntity<List<ExpenseView>> filterExpenseByGroupId(@PathVariable Long id, @RequestBody UserGroupFilterInput expenseInput) throws GroupValidationException, UserValidationException, ExpenseValidationException, UserNotFoundException, GroupNotFoundException {
        List<ExpenseView> expenses = expenseService.viewAllExpenseByGroupId(id, expenseInput);
        return ResponseEntity.ok(expenses);
    }

    @ExceptionHandler({GroupValidationException.class, UserValidationException.class, ExpenseValidationException.class})
    public ResponseEntity<String> handleClientException(Exception e) {
        log.warn("Exception ", e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler({UserNotFoundException.class, GroupNotFoundException.class})
    public ResponseEntity<String> handleNotFoundException(Exception e) {
        log.warn("Exception ", e);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        log.warn("Something Went Wrong", e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
}
