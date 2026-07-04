package com.example.expenseTracker.dto.respnse;

public class SavingsGoalResponseDto {

    private String goalName;
    private Double targetAmount;
    private Double savedAmount;
    private Double remainingAmount;
    private String status;

    public SavingsGoalResponseDto(String goalName,
                                  Double targetAmount,
                                  Double savedAmount,
                                  Double remainingAmount,
                                  String status) {
        this.goalName = goalName;
        this.targetAmount = targetAmount;
        this.savedAmount = savedAmount;
        this.remainingAmount = remainingAmount;
        this.status = status;
    }

    public String getGoalName() {
        return goalName;
    }

    public Double getTargetAmount() {
        return targetAmount;
    }

    public Double getSavedAmount() {
        return savedAmount;
    }

    public Double getRemainingAmount() {
        return remainingAmount;
    }

    public String getStatus() {
        return status;
    }
}