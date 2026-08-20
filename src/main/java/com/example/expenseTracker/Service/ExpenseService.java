package com.example.expenseTracker.service;

import com.example.expenseTracker.config.ApiResponse;
import com.example.expenseTracker.dto.respnse.DashboardResponseDto;
import com.example.expenseTracker.dto.request.ExpenseRequestDto;
import com.example.expenseTracker.dto.respnse.ExpenseResponseDto;
import com.example.expenseTracker.Entity.ExpenseEntity;
import com.example.expenseTracker.Entity.ProfileEntity;
import com.example.expenseTracker.repository.ExpenseRepository;
import com.example.expenseTracker.repository.ProfileRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final ProfileRepository profileRepository;

    public ExpenseService(ExpenseRepository expenseRepository,
                          ProfileRepository profileRepository) {
        this.expenseRepository = expenseRepository;
        this.profileRepository = profileRepository;
    }
    private static final Logger log = LoggerFactory.getLogger(ExpenseService.class);


    private ProfileEntity getLoggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        return profileRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Logged in user not found"));
    }

    public ApiResponse<Object> addExpense(ExpenseRequestDto request) {
        ProfileEntity user = getLoggedInUser();

        ExpenseEntity expense = new ExpenseEntity();
        expense.setTitle(request.getTitle());
        expense.setAmount(request.getAmount());
        expense.setCategory(request.getCategory());
        expense.setExpenseDate(request.getExpenseDate());
        expense.setDescription(request.getDescription()); 
        expense.setUser(user);

        expenseRepository.save(expense);
        return new ApiResponse<>("Expense added successfully", null);    }

    public List<ExpenseResponseDto> getMyExpenses() {
        ProfileEntity user = getLoggedInUser();
        List<ExpenseEntity> expenses = expenseRepository.findByUserId(user.getId());

        List<ExpenseResponseDto> responseList = new ArrayList<>();
        for (ExpenseEntity expense : expenses) {
            responseList.add(mapToResponse(expense));
        }

        return responseList;
    }

    public ExpenseResponseDto getExpenseById(Long expenseId) {
        ProfileEntity user = getLoggedInUser();
        ExpenseEntity expense = expenseRepository.findById(expenseId).orElse(null);

        if (expense == null) {
            return null;
        }

        if (!expense.getUser().getId().equals(user.getId())) {
            return null;
        }

        return mapToResponse(expense);
    }

    public String updateExpense(Long expenseId, ExpenseRequestDto request) {
        ProfileEntity user = getLoggedInUser();
        ExpenseEntity expense = expenseRepository.findById(expenseId).orElse(null);

        if (expense == null) {
            return "Expense not found";
        }

        if (!expense.getUser().getId().equals(user.getId())) {
            return "You are not allowed to update this expense";
        }

        expense.setTitle(request.getTitle());
        expense.setAmount(request.getAmount());
        expense.setCategory(request.getCategory());
        expense.setExpenseDate(request.getExpenseDate());
        expense.setDescription(request.getDescription());

        expenseRepository.save(expense);
        log.info("Expense updated. Expense ID: {}, User: {}", expenseId, user.getEmail());
        return "Expense updated successfully";
    }

    public String deleteExpense(Long expenseId) {
        ProfileEntity user = getLoggedInUser();
        ExpenseEntity expense = expenseRepository.findById(expenseId).orElse(null);

        if (expense == null) {
            return "Expense not found";
        }

        if (!expense.getUser().getId().equals(user.getId())) {
            return "You are not allowed to delete this expense";
        }

        expenseRepository.delete(expense);
        log.info("Expense deleted. Expense ID: {}, User: {}", expenseId, user.getEmail());
        return "Expense deleted successfully";
    }

    public DashboardResponseDto getDashboard() {
        ProfileEntity user = getLoggedInUser();
        List<ExpenseEntity> expenses = expenseRepository.findByUserId(user.getId());

        if (expenses.isEmpty()) {
            return new DashboardResponseDto(0.0, 0, 0.0, 0.0);
        }

        double total = 0;
        double max = Double.MIN_VALUE;
        double min = Double.MAX_VALUE;

        for (ExpenseEntity expense : expenses) {
            total += expense.getAmount();

            if (expense.getAmount() > max) {
                max = expense.getAmount();
            }

            if (expense.getAmount() < min) {
                min = expense.getAmount();
            }
        }

        return new DashboardResponseDto(total, expenses.size(), max, min);
    }

    public Map<String, Double> getMyCategoryWiseExpenses() {
        ProfileEntity user = getLoggedInUser();
        List<ExpenseEntity> expenses = expenseRepository.findByUserId(user.getId());

        Map<String, Double> map = new HashMap<>();

        for (ExpenseEntity expense : expenses) {
            map.put(
                    expense.getCategory(),
                    map.getOrDefault(expense.getCategory(), 0.0) + expense.getAmount()
            );
        }

        return map;
    }

    public List<ExpenseResponseDto> getRecentExpenses() {
        ProfileEntity user = getLoggedInUser();
        List<ExpenseEntity> expenses = expenseRepository.findByUserId(user.getId());

        expenses.sort((a, b) -> b.getExpenseDate().compareTo(a.getExpenseDate()));

        List<ExpenseResponseDto> result = new ArrayList<>();
        int limit = Math.min(5, expenses.size());

        for (int i = 0; i < limit; i++) {
            result.add(mapToResponse(expenses.get(i)));
        }
        return result;
    }

    private ExpenseResponseDto mapToResponse(ExpenseEntity expense) {
        return new ExpenseResponseDto(
                expense.getId(),
                expense.getTitle(),
                expense.getAmount(),
                expense.getCategory(),
                expense.getExpenseDate(),
                expense.getDescription(),
                expense.getUser().getId()
        );
    }
    public List<ExpenseResponseDto> filterByDate(LocalDate start, LocalDate end) {

        ProfileEntity user = getLoggedInUser();

        List<ExpenseEntity> expenses =
                expenseRepository.findByUserIdAndExpenseDateBetween(
                        user.getId(), start, end
                );

        List<ExpenseResponseDto> result = new ArrayList<>();

        for (ExpenseEntity e : expenses) {
            result.add(mapToResponse(e));
        }

        return result;
    }
    public Page<ExpenseEntity> getPaginated(int page, int size) {

        ProfileEntity user = getLoggedInUser();

        Pageable pageable = PageRequest.of(page, size);

        return expenseRepository.findByUserId(user.getId(), pageable);
    }
    public List<ExpenseResponseDto> searchByTitle(String title) {

        ProfileEntity user = getLoggedInUser();

        List<ExpenseEntity> expenses =
                expenseRepository.findByUserIdAndTitleContainingIgnoreCase(
                        user.getId(), title
                );

        List<ExpenseResponseDto> result = new ArrayList<>();

        for (ExpenseEntity e : expenses) {
            result.add(mapToResponse(e));
        }

        return result;
    }

}