package com.example.expenseTracker.controller;

import com.example.expenseTracker.dto.request.BudgetRequestDto;
import com.example.expenseTracker.dto.respnse.BudgetStatusDto;
import com.example.expenseTracker.service.BudgetService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/budget")
public class BudgetController {

    private final BudgetService budgetService;

    public BudgetController(BudgetService budgetService) {
        this.budgetService = budgetService;
    }

    @PostMapping("/set")
    public String setBudget(@Valid @RequestBody BudgetRequestDto request) {
        return budgetService.setBudget(request);
    }

    @GetMapping("/status")
    public BudgetStatusDto getBudgetStatus(
            @RequestParam int month,
            @RequestParam int year
    ) {
        return budgetService.getBudgetStatus(month, year);
    }
}