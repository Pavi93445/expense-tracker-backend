package com.example.expenseTracker.Repository;

import com.example.expenseTracker.Entity.SavingsGoalEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SavingsGoalRepository
        extends JpaRepository<SavingsGoalEntity, Long> {

    List<SavingsGoalEntity> findByUserId(Long userId);
}