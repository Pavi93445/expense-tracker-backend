package com.example.expenseTracker.Controller;

import org.springframework.web.bind.annotation.*;

import com.example.expenseTracker.Dto.AuthRequestDto;
import com.example.expenseTracker.Dto.LoginRequestDto;
import com.example.expenseTracker.Service.AuthService;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService service;

    // 🔹 Constructor Injection
    public AuthController(AuthService service) {
        this.service = service;
    }

    // 🔹 Register API
    @PostMapping("/register")
    public String register(@RequestBody AuthRequestDto request) {
        return service.register(request);
    }

    // 🔹 Login API
    @PostMapping("/login")
    public String login(@RequestBody LoginRequestDto request) {
        return service.login(request);
    }

    // 🔹 Email Verification API
    @GetMapping("/verify")
    public String verifyEmail(@RequestParam String token) {
        return service.verifyEmail(token);
    }
}