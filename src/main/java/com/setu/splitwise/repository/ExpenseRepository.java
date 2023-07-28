package com.setu.splitwise.repository;

import com.setu.splitwise.model.Expense;
import com.setu.splitwise.model.Group;
import com.setu.splitwise.model.UserGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    List<Expense> findByGroupId(Long groupId);

    List<Expense> findByExpenseAtBetweenAndGroupIdIn(Long fromDate, Long toDate, List<Long> groupIds);
}