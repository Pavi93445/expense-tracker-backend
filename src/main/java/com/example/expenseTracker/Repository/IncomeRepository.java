package com.example.expenseTracker.repository;

import com.example.expenseTracker.Entity.IncomeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface IncomeRepository extends JpaRepository<IncomeEntity, Long> {
    List<IncomeEntity> findByUserId(Long userId);

    List<IncomeEntity> findByUserIdAndIncomeDateBetween(
            Long userId,
            LocalDate start,
            LocalDate end
    );
    @Query("""
       SELECT i.source, SUM(i.amount)
       FROM IncomeEntity i
       WHERE i.user.id = :userId
       GROUP BY i.source
       """)
    List<Object[]> getIncomeAnalytics(Long userId);
}