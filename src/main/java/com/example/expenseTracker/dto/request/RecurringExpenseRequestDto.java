package com.example.expenseTracker.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class RecurringExpenseRequestDto {

    @NotBlank
    private String title;

    @NotNull
    @Positive
    private Double amount;

    @NotBlank
    private String category;

    @NotBlank
    private String frequency;

    private String description;

    public String getTitle() {
        return title;
    }

    public Double getAmount() {
        return amount;
    }

    public String getCategory() {
        return category;
    }

    public String getFrequency() {
        return frequency;
    }

    public String getDescription() {
        return description;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}