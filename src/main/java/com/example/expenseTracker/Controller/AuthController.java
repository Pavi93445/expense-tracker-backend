package com.example.expenseTracker.Controller;
import com.example.expenseTracker.Dto.AuthRequestDto;
import com.example.expenseTracker.Dto.ChangePasswordRequestDto;
import com.example.expenseTracker.Dto.LoginRequestDto;
import com.example.expenseTracker.Dto.ProfileResponseDto;
import com.example.expenseTracker.Dto.UpdateProfileRequestDto;
import com.example.expenseTracker.Service.AuthService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import com.example.expenseTracker.Dto.LoginResponseDto;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService service;

    public AuthController(AuthService service) {
        this.service = service;
    }

    @PostMapping("/register")
    public String register(@Valid @RequestBody AuthRequestDto request) {
        return service.register(request);
    }

    @PostMapping("/login")
    public LoginResponseDto login(@Valid @RequestBody LoginRequestDto request) {
        return service.login(request);
    }

    @GetMapping("/verify")
    public String verifyEmail(@RequestParam String token) {
        return service.verifyEmail(token);
    }

    @GetMapping("/profile/{id}")
    public ProfileResponseDto getProfileById(@PathVariable Long id) {
        return service.getProfileById(id);
    }

    @PutMapping("/update/{id}")
    public String updateProfile(@PathVariable Long id,
                                @Valid @RequestBody UpdateProfileRequestDto request) {
        return service.updateProfile(id, request);
    }

    @PutMapping("/change-password/{id}")
    public String changePassword(@PathVariable Long id,
                                 @Valid @RequestBody ChangePasswordRequestDto request) {
        return service.changePassword(id, request);
    }

    @DeleteMapping("/delete/{id}")
    public String deleteAccount(@PathVariable Long id) {
        return service.deleteAccount(id);
    }
}