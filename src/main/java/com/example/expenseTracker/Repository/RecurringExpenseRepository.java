package com.example.expenseTracker.Repository;

import com.example.expenseTracker.Entity.RecurringExpenseEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecurringExpenseRepository
        extends JpaRepository<RecurringExpenseEntity, Long> {

    List<RecurringExpenseEntity> findByUserId(Long userId);
}