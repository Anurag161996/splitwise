package com.setu.splitwise.model.input;

import com.setu.splitwise.constants.ExpenseType;
import com.setu.splitwise.model.UserContribution;
import lombok.Data;

import java.util.List;

@Data
public class ExpenseInput {

    private Long userId;

    private Double transactionAmount;

    private Long groupId;

    private ExpenseType expenseType;

    private String expenseName;

    private String description;

    private String expenseDate;

    private List<UserContribution> userContributions;

}
