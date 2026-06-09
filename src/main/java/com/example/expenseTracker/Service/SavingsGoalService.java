package com.example.expenseTracker.Service;

import com.example.expenseTracker.Dto.SavingsGoalRequestDto;
import com.example.expenseTracker.Dto.SavingsGoalResponseDto;
import com.example.expenseTracker.Entity.ProfileEntity;
import com.example.expenseTracker.Entity.SavingsGoalEntity;
import com.example.expenseTracker.Repository.ProfileRepository;
import com.example.expenseTracker.Repository.SavingsGoalRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SavingsGoalService {

    private final SavingsGoalRepository savingsGoalRepository;
    private final ProfileRepository profileRepository;

    public SavingsGoalService(
            SavingsGoalRepository savingsGoalRepository,
            ProfileRepository profileRepository
    ) {
        this.savingsGoalRepository = savingsGoalRepository;
        this.profileRepository = profileRepository;
    }

    private ProfileEntity getLoggedInUser() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        return profileRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public String createGoal(SavingsGoalRequestDto request) {

        ProfileEntity user = getLoggedInUser();

        SavingsGoalEntity goal = new SavingsGoalEntity();

        goal.setGoalName(request.getGoalName());
        goal.setTargetAmount(request.getTargetAmount());
        goal.setSavedAmount(0.0);
        goal.setUser(user);

        savingsGoalRepository.save(goal);

        return "Savings goal created successfully";
    }

    public List<SavingsGoalResponseDto> getMyGoals() {

        ProfileEntity user = getLoggedInUser();

        List<SavingsGoalEntity> goals =
                savingsGoalRepository.findByUserId(user.getId());

        List<SavingsGoalResponseDto> response =
                new ArrayList<>();

        for (SavingsGoalEntity goal : goals) {

            double remaining =
                    goal.getTargetAmount() - goal.getSavedAmount();

            String status;

            if (remaining <= 0) {
                status = "COMPLETED";
            } else {
                status = "IN_PROGRESS";
            }

            response.add(
                    new SavingsGoalResponseDto(
                            goal.getGoalName(),
                            goal.getTargetAmount(),
                            goal.getSavedAmount(),
                            remaining,
                            status
                    )
            );
        }

        return response;
    }

    public String addSavings(Long goalId, Double amount) {

        SavingsGoalEntity goal =
                savingsGoalRepository.findById(goalId)
                        .orElseThrow(() ->
                                new RuntimeException("Goal not found"));

        goal.setSavedAmount(
                goal.getSavedAmount() + amount
        );

        savingsGoalRepository.save(goal);

        return "Savings added successfully";
    }
}