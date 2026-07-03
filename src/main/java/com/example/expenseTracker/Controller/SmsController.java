package com.example.expenseTracker.controller;


import com.example.expenseTracker.dto.respnse.ParsedTransactionDto;
import com.example.expenseTracker.dto.request.SmsParseRequestDto;
import com.example.expenseTracker.service.SmsTransactionService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sms")
public class SmsController {

    private final SmsTransactionService smsTransactionService;

    public SmsController(SmsTransactionService smsTransactionService) {
        this.smsTransactionService = smsTransactionService;
    }

    @PostMapping("/parse")
    public ResponseEntity<ParsedTransactionDto> parseSms(@Valid @RequestBody SmsParseRequestDto request,
                                                         @RequestParam(defaultValue = "true") boolean autoSave) {
        ParsedTransactionDto result = smsTransactionService.processSms(request.getSmsText(), autoSave);
        return ResponseEntity.ok(result);
    }
}