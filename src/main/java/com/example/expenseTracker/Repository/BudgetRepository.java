package com.example.expenseTracker.Repository;

import com.example.expenseTracker.Entity.BudgetEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BudgetRepository extends JpaRepository<BudgetEntity, Long> {

    Optional<BudgetEntity> findByUserIdAndMonthAndYear(
            Long userId,
            int month,
            int year
    );
}