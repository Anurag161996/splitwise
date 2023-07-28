package com.setu.splitwise.model.view;


import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ExpenseView {

    private String date;
    private String expenseName;
    private String expenseDescription;
    private String groupName;
    private Double transactionAmount;
    private Double pendingAmount;
}
