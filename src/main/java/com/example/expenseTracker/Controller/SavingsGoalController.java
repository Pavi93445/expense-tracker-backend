package com.example.expenseTracker.Controller;

import com.example.expenseTracker.Dto.SavingsGoalRequestDto;
import com.example.expenseTracker.Dto.SavingsGoalResponseDto;
import com.example.expenseTracker.Service.SavingsGoalService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/goals")
public class SavingsGoalController {

    private final SavingsGoalService savingsGoalService;

    public SavingsGoalController(
            SavingsGoalService savingsGoalService
    ) {
        this.savingsGoalService = savingsGoalService;
    }

    @PostMapping("/create")
    public String createGoal(
            @Valid @RequestBody SavingsGoalRequestDto request
    ) {
        return savingsGoalService.createGoal(request);
    }

    @GetMapping("/my")
    public List<SavingsGoalResponseDto> getMyGoals() {
        return savingsGoalService.getMyGoals();
    }

    @PutMapping("/add-savings/{goalId}")
    public String addSavings(
            @PathVariable Long goalId,
            @RequestParam Double amount
    ) {
        return savingsGoalService.addSavings(goalId, amount);
    }
}