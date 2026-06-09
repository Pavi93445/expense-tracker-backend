package com.example.expenseTracker.Dto;

public class MonthlyReportDto {

    private int month;
    private int year;

    private double totalIncome;
    private double totalExpense;
    private double balance;

    public MonthlyReportDto(int month,
                            int year,
                            double totalIncome,
                            double totalExpense,
                            double balance) {
        this.month = month;
        this.year = year;
        this.totalIncome = totalIncome;
        this.totalExpense = totalExpense;
        this.balance = balance;
    }

    public int getMonth() {
        return month;
    }

    public int getYear() {
        return year;
    }

    public double getTotalIncome() {
        return totalIncome;
    }

    public double getTotalExpense() {
        return totalExpense;
    }

    public double getBalance() {
        return balance;
    }
}