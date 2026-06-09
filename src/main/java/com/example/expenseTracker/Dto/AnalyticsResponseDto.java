package com.example.expenseTracker.Dto;

public class AnalyticsResponseDto {

    private String name;
    private Double total;

    public AnalyticsResponseDto(String name, Double total) {
        this.name = name;
        this.total = total;
    }

    public String getName() {
        return name;
    }

    public Double getTotal() {
        return total;
    }
}