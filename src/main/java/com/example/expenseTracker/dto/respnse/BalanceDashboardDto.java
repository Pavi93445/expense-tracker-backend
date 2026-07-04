package com.example.expenseTracker.dto.respnse;

public class BalanceDashboardDto {

    private Double totalIncome;
    private Double totalExpense;
    private Double balance;
    private int totalIncomeTransactions;
    private int totalExpenseTransactions;

    public BalanceDashboardDto(Double totalIncome,
                               Double totalExpense,
                               Double balance,
                               int totalIncomeTransactions,
                               int totalExpenseTransactions) {
        this.totalIncome = totalIncome;
        this.totalExpense = totalExpense;
        this.balance = balance;
        this.totalIncomeTransactions = totalIncomeTransactions;
        this.totalExpenseTransactions = totalExpenseTransactions;
    }

    public Double getTotalIncome() {
        return totalIncome;
    }

    public Double getTotalExpense() {
        return totalExpense;
    }

    public Double getBalance() {
        return balance;
    }

    public int getTotalIncomeTransactions() {
        return totalIncomeTransactions;
    }

    public int getTotalExpenseTransactions() {
        return totalExpenseTransactions;
    }
}