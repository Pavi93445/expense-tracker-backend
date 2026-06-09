package com.example.expenseTracker.Repository;

import java.util.Optional;

import com.example.expenseTracker.Entity.ProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.expenseTracker.Entity.VerificationToken;

public interface VerificationTokenRepository
        extends JpaRepository<VerificationToken, Long> {

    Optional<VerificationToken> findByToken(String token);
    Optional<VerificationToken> findByUser(ProfileEntity user);

}