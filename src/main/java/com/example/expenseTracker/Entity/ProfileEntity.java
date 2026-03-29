package com.example.expenseTracker.Entity;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class ProfileEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    private String password;

    // 🔥 Email verification field
    private boolean verified;

    // 🔹 No-Args Constructor
    public ProfileEntity() {
    }

    // 🔹 All-Args Constructor
    public ProfileEntity(Long id,
                         String name,
                         String email,
                         String password,
                         boolean verified) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.verified = verified;
    }

    // 🔹 Getters & Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // 🔥 Verified getter
    public boolean isVerified() {
        return verified;
    }

    // 🔥 Verified setter
    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    // 🔹 toString
    @Override
    public String toString() {
        return "ProfileEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", verified=" + verified +
                '}';
    }
}