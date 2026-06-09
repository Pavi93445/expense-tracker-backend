package com.example.expenseTracker.Service;
import com.example.expenseTracker.Dto.BalanceDashboardDto;
import com.example.expenseTracker.Entity.ExpenseEntity;
import com.example.expenseTracker.Repository.ExpenseRepository;
import com.example.expenseTracker.Dto.AnalyticsResponseDto;
import java.util.ArrayList;

import com.example.expenseTracker.Dto.IncomeRequestDto;
import com.example.expenseTracker.Entity.IncomeEntity;
import com.example.expenseTracker.Entity.ProfileEntity;
import com.example.expenseTracker.Repository.IncomeRepository;
import com.example.expenseTracker.Repository.ProfileRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.example.expenseTracker.Dto.MonthlyReportDto;
import java.time.LocalDate;
import com.example.expenseTracker.Dto.TransactionResponseDto;
import java.util.Comparator;
import java.util.List;

@Service
public class IncomeService {

    private final IncomeRepository incomeRepository;
    private final ProfileRepository profileRepository;
    private final ExpenseRepository expenseRepository;

    public IncomeService(IncomeRepository incomeRepository,
                         ProfileRepository profileRepository,ExpenseRepository expenseRepository) {
        this.incomeRepository = incomeRepository;
        this.profileRepository = profileRepository;
        this.expenseRepository = expenseRepository;

    }

    private ProfileEntity getLoggedInUser() {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        return profileRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public String addIncome(IncomeRequestDto request) {
        ProfileEntity user = getLoggedInUser();

        IncomeEntity income = new IncomeEntity();
        income.setTitle(request.getTitle());
        income.setAmount(request.getAmount());
        income.setSource(request.getSource());
        income.setIncomeDate(request.getIncomeDate());
        income.setDescription(request.getDescription());
        income.setUser(user);

        incomeRepository.save(income);

        return "Income added successfully";
    }

    public List<IncomeEntity> getMyIncome() {
        ProfileEntity user = getLoggedInUser();
        return incomeRepository.findByUserId(user.getId());
    }
    public BalanceDashboardDto getBalanceDashboard() {

        ProfileEntity user = getLoggedInUser();

        List<IncomeEntity> incomes =
                incomeRepository.findByUserId(user.getId());

        List<ExpenseEntity> expenses =
                expenseRepository.findByUserId(user.getId());

        double totalIncome = 0;
        double totalExpense = 0;

        for (IncomeEntity income : incomes) {
            totalIncome += income.getAmount();
        }

        for (ExpenseEntity expense : expenses) {
            totalExpense += expense.getAmount();
        }

        double balance = totalIncome - totalExpense;

        return new BalanceDashboardDto(
                totalIncome,
                totalExpense,
                balance,
                incomes.size(),
                expenses.size()
        );
    }
    public MonthlyReportDto getMonthlyReport(int month, int year) {

        ProfileEntity user = getLoggedInUser();

        LocalDate startDate =
                LocalDate.of(year, month, 1);

        LocalDate endDate =
                startDate.withDayOfMonth(startDate.lengthOfMonth());

        List<IncomeEntity> incomes =
                incomeRepository.findByUserIdAndIncomeDateBetween(
                        user.getId(),
                        startDate,
                        endDate
                );

        List<ExpenseEntity> expenses =
                expenseRepository.findByUserIdAndExpenseDateBetween(
                        user.getId(),
                        startDate,
                        endDate
                );

        double totalIncome = 0;
        double totalExpense = 0;

        for (IncomeEntity income : incomes) {
            totalIncome += income.getAmount();
        }

        for (ExpenseEntity expense : expenses) {
            totalExpense += expense.getAmount();
        }

        double balance = totalIncome - totalExpense;

        return new MonthlyReportDto(
                month,
                year,
                totalIncome,
                totalExpense,
                balance
        );
    }
    public List<AnalyticsResponseDto> getExpenseAnalytics() {

        ProfileEntity user = getLoggedInUser();

        List<Object[]> result =
                expenseRepository.getExpenseAnalytics(user.getId());

        List<AnalyticsResponseDto> response =
                new ArrayList<>();

        for (Object[] row : result) {

            String category = (String) row[0];
            Double total = (Double) row[1];

            response.add(
                    new AnalyticsResponseDto(category, total)
            );
        }

        return response;
    }
    public List<AnalyticsResponseDto> getIncomeAnalytics() {

        ProfileEntity user = getLoggedInUser();

        List<Object[]> result =
                incomeRepository.getIncomeAnalytics(user.getId());

        List<AnalyticsResponseDto> response =
                new ArrayList<>();

        for (Object[] row : result) {

            String source = (String) row[0];
            Double total = (Double) row[1];

            response.add(
                    new AnalyticsResponseDto(source, total)
            );
        }

        return response;
    }
    public List<TransactionResponseDto> getRecentTransactions() {

        ProfileEntity user = getLoggedInUser();

        List<IncomeEntity> incomes =
                incomeRepository.findByUserId(user.getId());

        List<ExpenseEntity> expenses =
                expenseRepository.findByUserId(user.getId());

        List<TransactionResponseDto> transactions = new ArrayList<>();

        for (IncomeEntity income : incomes) {
            transactions.add(new TransactionResponseDto(
                    "INCOME",
                    income.getTitle(),
                    income.getAmount(),
                    income.getSource(),
                    income.getIncomeDate(),
                    income.getDescription()
            ));
        }

        for (ExpenseEntity expense : expenses) {
            transactions.add(new TransactionResponseDto(
                    "EXPENSE",
                    expense.getTitle(),
                    expense.getAmount(),
                    expense.getCategory(),
                    expense.getExpenseDate(),
                    expense.getDescription()
            ));
        }

        transactions.sort(
                Comparator.comparing(TransactionResponseDto::getDate).reversed()
        );

        if (transactions.size() > 5) {
            return transactions.subList(0, 5);
        }

        return transactions;
    }
}