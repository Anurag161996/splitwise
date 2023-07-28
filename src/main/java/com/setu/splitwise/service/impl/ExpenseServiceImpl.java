package com.setu.splitwise.service.impl;

import com.setu.splitwise.exceptions.ExpenseValidationException;
import com.setu.splitwise.exceptions.GroupNotFoundException;
import com.setu.splitwise.exceptions.GroupValidationException;
import com.setu.splitwise.exceptions.UserNotFoundException;
import com.setu.splitwise.exceptions.UserValidationException;
import com.setu.splitwise.model.Expense;
import com.setu.splitwise.model.Group;
import com.setu.splitwise.model.UserGroup;
import com.setu.splitwise.model.input.ExpenseDateFilterInput;
import com.setu.splitwise.model.input.ExpenseInput;
import com.setu.splitwise.model.input.UserGroupFilterInput;
import com.setu.splitwise.model.view.ExpenseView;
import com.setu.splitwise.repository.ExpenseRepository;
import com.setu.splitwise.service.ExpenseService;
import com.setu.splitwise.service.GroupService;
import com.setu.splitwise.service.UserGroupService;
import com.setu.splitwise.utils.DateUtils;
import com.setu.splitwise.utils.ExpenseUtils;
import com.setu.splitwise.validators.ExpenseStrategyValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ExpenseServiceImpl implements ExpenseService {

    @Autowired
    ExactExpenseServiceImpl exactExpenseService;

    @Autowired
    EqualExpenseServiceImpl equalExpenseService;

    @Autowired
    ExpenseRepository expenseRepository;

    @Autowired
    GroupService groupService;

    @Autowired
    UserGroupService userGroupService;

    @Autowired
    ExpenseStrategyValidation expenseStrategyValidation;

    @Override
    public Expense addExpense(ExpenseInput expenseInput) throws UserValidationException, GroupValidationException, ExpenseValidationException, UserNotFoundException, GroupNotFoundException {
        Expense expense = processExpense(expenseInput);
        return expenseRepository.save(expense);
    }

    @Override
    public List<ExpenseView> viewAllExpenseByGroupId(Long userId, UserGroupFilterInput input) throws GroupValidationException, UserValidationException, ExpenseValidationException, UserNotFoundException, GroupNotFoundException {
        expenseStrategyValidation.validateGroupFilterInput(userId, input);
        List<ExpenseView> expenseViews = new ArrayList<>();
        Group group = groupService.getGroupsByIds(input.getGroupId());
        UserGroup userGroup = userGroupService.findByGroupIdAndUserId(input.getGroupId(), userId);
        if(Objects.isNull(userGroup))
            throw new ExpenseValidationException("User is not part of that group");
        List<Expense> expenseList = expenseRepository.findByGroupId(input.getGroupId());
        if(CollectionUtils.isEmpty(expenseList)) return expenseViews;
        expenseList.forEach(expense -> {
            expenseViews.add(ExpenseView.builder()
                            .expenseName(expense.getExpenseName())
                            .expenseDescription(expense.getDescription())
                            .transactionAmount(expense.getTransactionAmount())
                            .date(DateUtils.convertEpochToFormattedDate(expense.getExpenseAt()))
                            .pendingAmount(ExpenseUtils.roundToTwoDecimalPlaces(calculatePendingAmount(expense, userId)))
                            .groupName(group.getGroupName())
                    .build());
        });
        return expenseViews;
    }

    @Override
    public List<ExpenseView> viewAllExpenseByUserId(Long userId, ExpenseDateFilterInput input) throws GroupValidationException, UserValidationException, ExpenseValidationException, UserNotFoundException {
        expenseStrategyValidation.validateExpenseFilterInput(userId, input);
        List<ExpenseView> expenseViews = new ArrayList<>();
        List<UserGroup> userGroup = userGroupService.findUserGroupByUserId(userId);
        List<Long> groupIds = userGroup.stream().map(item -> item.getGroupId()).collect(Collectors.toList());
        List<Expense> expenseList = expenseRepository.findByExpenseAtBetweenAndGroupIdIn(DateUtils.getStartOfTheDayEpoch(input.getFromDate()), DateUtils.getEndOfTheDayEpoch(input.getToDate()), groupIds);
        if(CollectionUtils.isEmpty(expenseList)) return expenseViews;
        Map<Long, Group> groupMap = groupService.getGroupsByIds(groupIds);
        expenseList.forEach(expense -> {
            expenseViews.add(ExpenseView.builder()
                    .expenseName(expense.getExpenseName())
                    .expenseDescription(expense.getDescription())
                    .transactionAmount(ExpenseUtils.roundToTwoDecimalPlaces(expense.getTransactionAmount()))
                    .date(DateUtils.convertEpochToFormattedDate(expense.getExpenseAt()))
                    .pendingAmount(ExpenseUtils.roundToTwoDecimalPlaces(calculatePendingAmount(expense, userId)))
                    .groupName(groupMap.get(expense.getGroupId()).getGroupName())
                    .build());
        });
        return expenseViews;
    }

    @Override
    public List<Expense> getAllExpenses() {
        return expenseRepository.findAll();
    }

    @Override
    public void deleteExpenses(Long id) {
        expenseRepository.deleteById(id);
    }

    private Expense processExpense(ExpenseInput expenseInput) throws ExpenseValidationException, UserValidationException, GroupValidationException, UserNotFoundException, GroupNotFoundException {
        if(Objects.isNull(expenseInput))
            throw new ExpenseValidationException("Invalid expenseInput");
        switch(expenseInput.getExpenseType()) {
            case EQUAL:
                return equalExpenseService.calculateExpense(expenseInput);
            case EXACT:
                return exactExpenseService.calculateExpense(expenseInput);
            default:
                throw new ExpenseValidationException("Invalid expense type");
        }

    }

    private Double calculatePendingAmount(Expense expense, Long userId) {
        switch(expense.getExpenseType()) {
            case EQUAL:
                return equalExpenseService.calculatePendingAmount(expense, userId);
            case EXACT:
            default:
                return exactExpenseService.calculatePendingAmount(expense, userId);
        }

    }


}
