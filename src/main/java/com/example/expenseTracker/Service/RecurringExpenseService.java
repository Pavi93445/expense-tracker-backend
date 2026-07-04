package com.example.expenseTracker.service;

import com.example.expenseTracker.dto.request.RecurringExpenseRequestDto;
import com.example.expenseTracker.Entity.ProfileEntity;
import com.example.expenseTracker.Entity.RecurringExpenseEntity;
import com.example.expenseTracker.repository.ProfileRepository;
import com.example.expenseTracker.repository.RecurringExpenseRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecurringExpenseService {

    private final RecurringExpenseRepository recurringExpenseRepository;
    private final ProfileRepository profileRepository;

    public RecurringExpenseService(
            RecurringExpenseRepository recurringExpenseRepository,
            ProfileRepository profileRepository
    ) {
        this.recurringExpenseRepository = recurringExpenseRepository;
        this.profileRepository = profileRepository;
    }

    private ProfileEntity getLoggedInUser() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        return profileRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public String addRecurringExpense(
            RecurringExpenseRequestDto request
    ) {

        ProfileEntity user = getLoggedInUser();

        RecurringExpenseEntity recurring =
                new RecurringExpenseEntity();

        recurring.setTitle(request.getTitle());
        recurring.setAmount(request.getAmount());
        recurring.setCategory(request.getCategory());
        recurring.setFrequency(request.getFrequency());
        recurring.setDescription(request.getDescription());
        recurring.setUser(user);

        recurringExpenseRepository.save(recurring);

        return "Recurring expense added successfully";
    }

    public List<RecurringExpenseEntity> getMyRecurringExpenses() {

        ProfileEntity user = getLoggedInUser();

        return recurringExpenseRepository
                .findByUserId(user.getId());
    }
}