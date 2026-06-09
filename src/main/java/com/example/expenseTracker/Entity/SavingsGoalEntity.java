package com.example.expenseTracker.Entity;

import jakarta.persistence.*;

@Entity
@Table(name = "savings_goals")
public class SavingsGoalEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String goalName;
    private Double targetAmount;
    private Double savedAmount;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private ProfileEntity user;

    public Long getId() {
        return id;
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

    public ProfileEntity getUser() {
        return user;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setGoalName(String goalName) {
        this.goalName = goalName;
    }

    public void setTargetAmount(Double targetAmount) {
        this.targetAmount = targetAmount;
    }

    public void setSavedAmount(Double savedAmount) {
        this.savedAmount = savedAmount;
    }

    public void setUser(ProfileEntity user) {
        this.user = user;
    }
}