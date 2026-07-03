package com.example.expenseTracker.controller;

import com.example.expenseTracker.dto.request.RecurringExpenseRequestDto;
import com.example.expenseTracker.Entity.RecurringExpenseEntity;
import com.example.expenseTracker.service.RecurringExpenseService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/recurring")
public class RecurringExpenseController {

    private final RecurringExpenseService recurringExpenseService;

    public RecurringExpenseController(
            RecurringExpenseService recurringExpenseService
    ) {
        this.recurringExpenseService = recurringExpenseService;
    }

    @PostMapping("/add")
    public String addRecurringExpense(
            @Valid @RequestBody RecurringExpenseRequestDto request
    ) {
        return recurringExpenseService
                .addRecurringExpense(request);
    }

    @GetMapping("/my")
    public List<RecurringExpenseEntity> getMyRecurringExpenses() {
        return recurringExpenseService.getMyRecurringExpenses();
    }
}