package com.example.expenseTracker.Service;

import com.example.expenseTracker.Dto.BudgetRequestDto;
import com.example.expenseTracker.Dto.BudgetStatusDto;
import com.example.expenseTracker.Entity.BudgetEntity;
import com.example.expenseTracker.Entity.ExpenseEntity;
import com.example.expenseTracker.Entity.ProfileEntity;
import com.example.expenseTracker.Repository.BudgetRepository;
import com.example.expenseTracker.Repository.ExpenseRepository;
import com.example.expenseTracker.Repository.ProfileRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class BudgetService {

    private final BudgetRepository budgetRepository;
    private final ExpenseRepository expenseRepository;
    private final ProfileRepository profileRepository;

    public BudgetService(BudgetRepository budgetRepository,
                         ExpenseRepository expenseRepository,
                         ProfileRepository profileRepository) {
        this.budgetRepository = budgetRepository;
        this.expenseRepository = expenseRepository;
        this.profileRepository = profileRepository;
    }

    private ProfileEntity getLoggedInUser() {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        return profileRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public String setBudget(BudgetRequestDto request) {

        ProfileEntity user = getLoggedInUser();

        BudgetEntity budget = budgetRepository
                .findByUserIdAndMonthAndYear(
                        user.getId(),
                        request.getMonth(),
                        request.getYear()
                )
                .orElse(new BudgetEntity());

        budget.setMonth(request.getMonth());
        budget.setYear(request.getYear());
        budget.setAmount(request.getAmount());
        budget.setUser(user);

        budgetRepository.save(budget);

        return "Budget saved successfully";
    }

    public BudgetStatusDto getBudgetStatus(int month, int year) {

        ProfileEntity user = getLoggedInUser();

        BudgetEntity budget = budgetRepository
                .findByUserIdAndMonthAndYear(user.getId(), month, year)
                .orElseThrow(() -> new RuntimeException("Budget not found"));

        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());

        List<ExpenseEntity> expenses =
                expenseRepository.findByUserIdAndExpenseDateBetween(
                        user.getId(),
                        startDate,
                        endDate
                );

        double totalExpense = 0;

        for (ExpenseEntity expense : expenses) {
            totalExpense += expense.getAmount();
        }

        double remaining = budget.getAmount() - totalExpense;

        String status;

        if (remaining < 0) {
            status = "OVER_BUDGET";
        } else if (remaining <= budget.getAmount() * 0.2) {
            status = "WARNING";
        } else {
            status = "SAFE";
        }

        return new BudgetStatusDto(
                month,
                year,
                budget.getAmount(),
                totalExpense,
                remaining,
                status
        );
    }
}