
package com.example.expenseTracker.Entity;

import jakarta.persistence.*;

@Entity
@Table(name = "recurring_expenses")
public class RecurringExpenseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private Double amount;
    private String category;
    private String frequency; // MONTHLY / WEEKLY

    private String description;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private ProfileEntity user;

    public Long getId() {
        return id;
    }

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

    public ProfileEntity getUser() {
        return user;
    }

    public void setId(Long id) {
        this.id = id;
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

    public void setUser(ProfileEntity user) {
        this.user = user;
    }
}