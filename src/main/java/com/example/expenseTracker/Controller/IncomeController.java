package com.example.expenseTracker.Controller;

import com.example.expenseTracker.Dto.IncomeRequestDto;
import com.example.expenseTracker.Entity.IncomeEntity;
import com.example.expenseTracker.Service.IncomeService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import com.example.expenseTracker.Dto.BalanceDashboardDto;
import com.example.expenseTracker.Dto.MonthlyReportDto;
import java.util.List;
import com.example.expenseTracker.Dto.AnalyticsResponseDto;
import com.example.expenseTracker.Dto.TransactionResponseDto;

import java.util.List;

@RestController
@RequestMapping("/income")
public class IncomeController {

    private final IncomeService incomeService;

    public IncomeController(IncomeService incomeService) {
        this.incomeService = incomeService;
    }

    @PostMapping("/add")
    public String addIncome(@Valid @RequestBody IncomeRequestDto request) {
        return incomeService.addIncome(request);
    }

    @GetMapping("/my")
    public List<IncomeEntity> getMyIncome() {
        return incomeService.getMyIncome();
    }

    @GetMapping("/dashboard")
    public BalanceDashboardDto getBalanceDashboard() {
        return incomeService.getBalanceDashboard();
    }
    @GetMapping("/monthly-report")
    public MonthlyReportDto getMonthlyReport(
            @RequestParam int month,
            @RequestParam int year
    ) {
        return incomeService.getMonthlyReport(month, year);
    }
    @GetMapping("/expense-analytics")
    public List<AnalyticsResponseDto> getExpenseAnalytics() {
        return incomeService.getExpenseAnalytics();
    }

    @GetMapping("/income-analytics")
    public List<AnalyticsResponseDto> getIncomeAnalytics() {
        return incomeService.getIncomeAnalytics();
    }
    @GetMapping("/recent-transactions")
    public List<TransactionResponseDto> getRecentTransactions() {
        return incomeService.getRecentTransactions();
    }
}