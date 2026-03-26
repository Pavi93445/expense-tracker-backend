package com.example.expenseTracker.Service;

import com.example.expenseTracker.Dto.AuthRequestDto;
import com.example.expenseTracker.Repository.ProfileRepository;
import org.springframework.stereotype.Service;
import com.example.expenseTracker.Entity.ProfileEntity;

@Service
public class AuthService {

    private final ProfileRepository repository;

    // 🔹 Constructor Injection (manual instead of @RequiredArgsConstructor)
    public AuthService(ProfileRepository repository) {
        this.repository = repository;
    }

    public String register(AuthRequestDto request) {

        // 🔹 check email already exists
        if (repository.findByEmail(request.getEmail()).isPresent()) {
            return "Email already exists";
        }

        // 🔹 Create object manually (NO Builder)
        ProfileEntity user = new ProfileEntity();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());

        // 🔹 Save to DB
        repository.save(user);

        return "User Registered Successfully";
    }
}