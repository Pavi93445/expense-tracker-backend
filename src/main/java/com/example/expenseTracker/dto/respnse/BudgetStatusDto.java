package com.example.expenseTracker.dto.respnse;

public class BudgetStatusDto {

    private int month;
    private int year;
    private Double budgetAmount;
    private Double totalExpense;
    private Double remainingAmount;
    private String status;

    public BudgetStatusDto(int month,
                           int year,
                           Double budgetAmount,
                           Double totalExpense,
                           Double remainingAmount,
                           String status) {
        this.month = month;
        this.year = year;
        this.budgetAmount = budgetAmount;
        this.totalExpense = totalExpense;
        this.remainingAmount = remainingAmount;
        this.status = status;
    }

    public int getMonth() {
        return month;
    }

    public int getYear() {
        return year;
    }

    public Double getBudgetAmount() {
        return budgetAmount;
    }

    public Double getTotalExpense() {
        return totalExpense;
    }

    public Double getRemainingAmount() {
        return remainingAmount;
    }

    public String getStatus() {
        return status;
    }
}