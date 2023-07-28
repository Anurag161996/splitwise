package com.setu.splitwise.model;

import com.setu.splitwise.constants.ExpenseType;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.Data;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.List;

@Data
@Entity
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
public class Expense {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    private Long userId;

    private Double transactionAmount;

    private Long groupId;

    @Enumerated(EnumType.STRING)
    private ExpenseType expenseType;

    private String expenseName;

    private String description;

    private Long expenseAt;

    private Long createdAt;

    private Long updatedAt;

    @Column(columnDefinition = "jsonb")
    @Type(type = "jsonb")
    private List<UserContribution> userContributions;

}
