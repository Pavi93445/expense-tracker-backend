package com.example.expenseTracker.Entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;

@Entity
@Table(name = "verification_tokens")
public class VerificationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String token;

    private LocalDateTime expiryTime;

    @OneToOne
    @JoinColumn(name = "user_id")
    private ProfileEntity user;

    // 🔹 No-Args Constructor
    public VerificationToken() {
    }

    // 🔹 All-Args Constructor
    public VerificationToken(Long id,
                             String token,
                             LocalDateTime expiryTime,
                             ProfileEntity user) {
        this.id = id;
        this.token = token;
        this.expiryTime = expiryTime;
        this.user = user;
    }

    // 🔹 Getters

    public Long getId() {
        return id;
    }

    public String getToken() {
        return token;
    }

    public LocalDateTime getExpiryTime() {
        return expiryTime;
    }

    public ProfileEntity getUser() {
        return user;
    }

    // 🔹 Setters

    public void setId(Long id) {
        this.id = id;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setExpiryTime(LocalDateTime expiryTime) {
        this.expiryTime = expiryTime;
    }

    public void setUser(ProfileEntity user) {
        this.user = user;
    }
}