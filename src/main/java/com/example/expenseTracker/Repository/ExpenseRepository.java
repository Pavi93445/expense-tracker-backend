package com.example.expenseTracker.Repository;

import com.example.expenseTracker.Entity.ExpenseEntity;
import com.example.expenseTracker.Entity.ProfileEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.time.LocalDate;
import java.util.List;

public interface ExpenseRepository extends JpaRepository<ExpenseEntity, Long> {

    List<ExpenseEntity> findByUser(ProfileEntity user);

    List<ExpenseEntity> findByUserId(Long userId);
    List<ExpenseEntity> findByUserIdAndExpenseDateBetween(
            Long userId,
            LocalDate start,
            LocalDate end
    );

    List<ExpenseEntity> findByUserIdAndTitleContainingIgnoreCase(Long userId, String title);
    Page<ExpenseEntity> findByUserId(Long userId, Pageable pageable);

    @Query("""
       SELECT e.category, SUM(e.amount)
       FROM ExpenseEntity e
       WHERE e.user.id = :userId
       GROUP BY e.category
       """)
    List<Object[]> getExpenseAnalytics(Long userId);
}