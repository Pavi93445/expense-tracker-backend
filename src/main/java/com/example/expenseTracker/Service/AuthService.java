package com.example.expenseTracker.service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import com.example.expenseTracker.dto.request.AuthRequestDto;
import com.example.expenseTracker.dto.request.ChangePasswordRequestDto;
import com.example.expenseTracker.dto.request.LoginRequestDto;
import com.example.expenseTracker.dto.request.UpdateProfileRequestDto;
import com.example.expenseTracker.dto.respnse.ProfileResponseDto;
import com.example.expenseTracker.repository.ProfileRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.expenseTracker.Entity.ProfileEntity;
import com.example.expenseTracker.Entity.VerificationToken;
import com.example.expenseTracker.repository.VerificationTokenRepository;
import com.example.expenseTracker.dto.respnse.LoginResponseDto;


@Service
public class AuthService {

    private final ProfileRepository repository;
    private final VerificationTokenRepository tokenRepository;
    private final EmailService emailService;
    private final JwtService jwtService;

    public AuthService(ProfileRepository repository,
                       VerificationTokenRepository tokenRepository,
                       EmailService emailService, JwtService jwtService) {
        this.repository = repository;
        this.tokenRepository = tokenRepository;
        this.emailService = emailService;
        this.jwtService = jwtService;
    }

    public String register(AuthRequestDto request) {

        Optional<ProfileEntity> existingUser = repository.findByEmail(request.getEmail());
        if (existingUser.isPresent()) {
            return "Email already exists";
        }

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        ProfileEntity user = new ProfileEntity();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(encoder.encode(request.getPassword()));
        user.setVerified(false);

        ProfileEntity savedUser = repository.save(user);

        String tokenValue = UUID.randomUUID().toString();

        VerificationToken token = new VerificationToken();
        token.setToken(tokenValue);
        token.setExpiryTime(LocalDateTime.now().plusMinutes(30));
        token.setUser(savedUser);

        tokenRepository.save(token);

        emailService.sendVerificationEmail(savedUser.getEmail(), tokenValue);

        return "User registered successfully. Please verify your email.";
    }
    public LoginResponseDto login(LoginRequestDto request) {
        ProfileEntity user = repository.findByEmail(request.getEmail()).orElse(null);

        if (user == null) {
            return new LoginResponseDto("User not found", null);
        }

        if (!user.isVerified()) {
            return new LoginResponseDto("Please verify your email first", null);
        }

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        if (!encoder.matches(request.getPassword(), user.getPassword())) {
            return new LoginResponseDto("Invalid password", null);
        }

        String token = jwtService.generateToken(user.getEmail());

        return new LoginResponseDto("Login Successful", token);
    }
    public String verifyEmail(String tokenValue) {

        VerificationToken token = tokenRepository.findByToken(tokenValue).orElse(null);

        if (token == null) {
            return "Invalid verification token";
        }

        if (token.getExpiryTime().isBefore(LocalDateTime.now())) {
            return "Verification token expired";
        }

        ProfileEntity user = token.getUser();
        user.setVerified(true);
        repository.save(user);

        tokenRepository.delete(token);

        return "Email verified successfully";
    }

    public ProfileResponseDto getProfileById(Long id) {
        ProfileEntity user = repository.findById(id).orElse(null);

        if (user == null) {
            return null;
        }

        ProfileResponseDto response = new ProfileResponseDto();
        response.setId(user.getId());
        response.setName(user.getName());
        response.setEmail(user.getEmail());

        return response;
    }
    public String updateProfile(Long id, UpdateProfileRequestDto request) {
        ProfileEntity user = repository.findById(id).orElse(null);

        if (user == null) {
            return "User not found";
        }

        // Email already used by another user ah check pannrom
        Optional<ProfileEntity> existingUser = repository.findByEmail(request.getEmail());

        if (existingUser.isPresent() && !existingUser.get().getId().equals(id)) {
            return "Email already exists";
        }

        user.setName(request.getName());
        user.setEmail(request.getEmail());

        repository.save(user);

        return "Profile updated successfully";
    }
    public String changePassword(Long id, ChangePasswordRequestDto request) {

        ProfileEntity user = repository.findById(id).orElse(null);

        if (user == null) {
            return "User not found";
        }

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        // old password match aagudha check
        if (!encoder.matches(request.getOldPassword(), user.getPassword())) {
            return "Old password is incorrect";
        }

        // optional small validation
        if (request.getNewPassword() == null || request.getNewPassword().length() < 6) {
            return "New password must be at least 6 characters";
        }

        // same password ah veika koodadhu
        if (encoder.matches(request.getNewPassword(), user.getPassword())) {
            return "New password cannot be same as old password";
        }

        user.setPassword(encoder.encode(request.getNewPassword()));
        repository.save(user);

        return "Password changed successfully";
    }
    public String deleteAccount(Long id) {

        ProfileEntity user = repository.findById(id).orElse(null);

        if (user == null) {
            return "User not found";
        }

        VerificationToken verificationToken = tokenRepository.findByUser(user).orElse(null);

        if (verificationToken != null) {
            tokenRepository.delete(verificationToken);
        }

        repository.delete(user);

        return "Account deleted successfully";
    }


}