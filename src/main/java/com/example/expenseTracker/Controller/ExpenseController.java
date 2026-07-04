package com.example.expenseTracker.controller;

import com.example.expenseTracker.config.ApiResponse;
import com.example.expenseTracker.dto.request.ExpenseRequestDto;
import com.example.expenseTracker.dto.respnse.ExpenseResponseDto;
import com.example.expenseTracker.Entity.ExpenseEntity;
import com.example.expenseTracker.service.ExpenseService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/expenses")
public class ExpenseController {

    private final ExpenseService expenseService;

    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    // Add expense for logged-in user
    @PostMapping("/add")
    public ApiResponse<Object> addExpense(@Valid @RequestBody ExpenseRequestDto request) {
        return expenseService.addExpense(request);
    }
    // Get all expenses of logged-in user
    @GetMapping("/my")
    public List<ExpenseResponseDto> getMyExpenses() {
        return expenseService.getMyExpenses();
    }

    // Get one expense by id
    @GetMapping("/{expenseId}")
    public Object getExpenseById(@PathVariable Long expenseId) {
        ExpenseResponseDto expense = expenseService.getExpenseById(expenseId);

        if (expense == null) {
            return "Expense not found or access denied";
        }

        return expense;
    }

    // Update expense
    @PutMapping("/update/{expenseId}")
    public String updateExpense(@PathVariable Long expenseId,
                                @Valid @RequestBody ExpenseRequestDto request) {
        return expenseService.updateExpense(expenseId, request);
    }

    // Delete expense
    @DeleteMapping("/delete/{expenseId}")
    public String deleteExpense(@PathVariable Long expenseId) {
        return expenseService.deleteExpense(expenseId);
    }

    // Category-wise summary for logged-in user
    @GetMapping("/my/category")
    public Map<String, Double> getMyCategoryWiseExpenses() {
        return expenseService.getMyCategoryWiseExpenses();
    }
    @GetMapping("/filter")
    public List<ExpenseResponseDto> filterByDate(
            @RequestParam LocalDate start,
            @RequestParam LocalDate end
    ) {
        return expenseService.filterByDate(start, end);
    }
    @GetMapping("/my/paginated")
    public Page<ExpenseEntity> getPaginated(
            @RequestParam int page,
            @RequestParam int size
    ) {
        return expenseService.getPaginated(page, size);
    }
    @GetMapping("/search")
    public List<ExpenseResponseDto> search(@RequestParam String title) {
        return expenseService.searchByTitle(title);
    }
}