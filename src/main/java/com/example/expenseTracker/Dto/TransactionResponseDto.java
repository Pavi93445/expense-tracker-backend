package com.example.expenseTracker.Dto;

import java.time.LocalDate;

public class TransactionResponseDto {

    private String type;
    private String title;
    private Double amount;
    private String categoryOrSource;
    private LocalDate date;
    private String description;

    public TransactionResponseDto(String type, String title, Double amount,
                                  String categoryOrSource, LocalDate date,
                                  String description) {
        this.type = type;
        this.title = title;
        this.amount = amount;
        this.categoryOrSource = categoryOrSource;
        this.date = date;
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public String getTitle() {
        return title;
    }

    public Double getAmount() {
        return amount;
    }

    public String getCategoryOrSource() {
        return categoryOrSource;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }
}