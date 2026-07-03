package com.example.expenseTracker.service;

import com.example.expenseTracker.dto.request.ExpenseRequestDto;
import com.example.expenseTracker.dto.respnse.ParsedTransactionDto;
import com.example.expenseTracker.enums.TransactionType;
// adjust to your actual package

import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class SmsTransactionService {

    private final SmsParserService smsParserService;
    private final CategoryClassifierService categoryClassifierService;
    private final ExpenseService expenseService;

    public SmsTransactionService(SmsParserService smsParserService,
                                 CategoryClassifierService categoryClassifierService,
                                 ExpenseService expenseService) {
        this.smsParserService = smsParserService;
        this.categoryClassifierService = categoryClassifierService;
        this.expenseService = expenseService;
    }

    public ParsedTransactionDto processSms(String smsText, boolean autoSave) {
        ParsedTransactionDto parsed = smsParserService.parse(smsText);

        if (!parsed.isParsedSuccessfully()) {
            return parsed; // don't proceed if extraction failed
        }

        String category = categoryClassifierService.classify(parsed.getMerchant());
        parsed.setCategory(category);

        // Only auto-create an expense for DEBIT transactions
        if (autoSave && parsed.getTransactionType() == TransactionType.DEBIT) {
            ExpenseRequestDto expenseRequest = new ExpenseRequestDto();
            expenseRequest.setTitle(parsed.getMerchant());
            expenseRequest.setAmount(parsed.getAmount().doubleValue());
            expenseRequest.setCategory(category);
            expenseRequest.setExpenseDate(LocalDate.now());
            expenseRequest.setDescription("Auto-captured from SMS: " + smsText);

            expenseService.addExpense(expenseRequest); // reuse your existing service method
        }

        return parsed;
    }
}