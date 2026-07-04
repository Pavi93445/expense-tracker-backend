package com.example.expenseTracker.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDate;

public class IncomeRequestDto {

    @NotBlank(message = "Title is required")
    private String title;

    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be greater than 0")
    private Double amount;

    @NotBlank(message = "Source is required")
    private String source;

    @NotNull(message = "Income date is required")
    private LocalDate incomeDate;

    private String description;

    public String getTitle() { return title; }
    public Double getAmount() { return amount; }
    public String getSource() { return source; }
    public LocalDate getIncomeDate() { return incomeDate; }
    public String getDescription() { return description; }

    public void setTitle(String title) { this.title = title; }
    public void setAmount(Double amount) { this.amount = amount; }
    public void setSource(String source) { this.source = source; }
    public void setIncomeDate(LocalDate incomeDate) { this.incomeDate = incomeDate; }
    public void setDescription(String description) { this.description = description; }
}