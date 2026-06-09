package com.example.expenseTracker.Dto;

public class DashboardResponseDto {

    private Double totalExpense;
    private int totalTransactions;
    private Double highestExpense;
    private Double lowestExpense;

    public DashboardResponseDto(Double totalExpense, int totalTransactions,
                                Double highestExpense, Double lowestExpense) {
        this.totalExpense = totalExpense;
        this.totalTransactions = totalTransactions;
        this.highestExpense = highestExpense;
        this.lowestExpense = lowestExpense;
    }

    public Double getTotalExpense() {
        return totalExpense;
    }

    public int getTotalTransactions() {
        return totalTransactions;
    }

    public Double getHighestExpense() {
        return highestExpense;
    }

    public Double getLowestExpense() {
        return lowestExpense;
    }
}