package com.example.expenseTracker.Controller;

import com.example.expenseTracker.Dto.RecurringExpenseRequestDto;
import com.example.expenseTracker.Entity.RecurringExpenseEntity;
import com.example.expenseTracker.Service.RecurringExpenseService;
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