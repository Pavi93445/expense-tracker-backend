package com.example.expenseTracker.Entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "income")
public class IncomeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private Double amount;
    private String source;
    private LocalDate incomeDate;
    private String description;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private ProfileEntity user;

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public Double getAmount() { return amount; }
    public String getSource() { return source; }
    public LocalDate getIncomeDate() { return incomeDate; }
    public String getDescription() { return description; }
    public ProfileEntity getUser() { return user; }

    public void setId(Long id) { this.id = id; }
    public void setTitle(String title) { this.title = title; }
    public void setAmount(Double amount) { this.amount = amount; }
    public void setSource(String source) { this.source = source; }
    public void setIncomeDate(LocalDate incomeDate) { this.incomeDate = incomeDate; }
    public void setDescription(String description) { this.description = description; }
    public void setUser(ProfileEntity user) { this.user = user; }
}