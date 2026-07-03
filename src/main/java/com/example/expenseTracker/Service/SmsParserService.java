package com.example.expenseTracker.service;

import com.example.expenseTracker.dto.respnse.ParsedTransactionDto;
import com.example.expenseTracker.enums.TransactionType;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class SmsParserService {

    // Matches: Rs.450.00 | Rs 1,299 | INR 5,000.00 | Rs.200
    private static final Pattern AMOUNT_PATTERN = Pattern.compile(
            "(?:Rs\\.?|INR)\\s?([0-9]+(?:,[0-9]{2,3})*(?:\\.[0-9]{1,2})?)",
            Pattern.CASE_INSENSITIVE
    );

    // Matches "to VPA merchant@bank" or "to Merchant Name" or "paid to Merchant" or "spent at Merchant"
    private static final Pattern MERCHANT_VPA_PATTERN = Pattern.compile(
            "(?:to|paid to|at)\\s+(?:VPA\\s+)?([a-zA-Z0-9._@\\s]+?)(?:\\s+via|\\.|,|\\son|$)",
            Pattern.CASE_INSENSITIVE
    );

    // Added: withdrawn, deducted (common ATM/bank phrasing)
    private static final Pattern DEBIT_KEYWORDS = Pattern.compile(
            "debited|paid|spent|purchase|withdrawn|deducted", Pattern.CASE_INSENSITIVE
    );

    // Added: deposited, cashback (common credit phrasing)
    private static final Pattern CREDIT_KEYWORDS = Pattern.compile(
            "credited|received|refund|deposited|cashback", Pattern.CASE_INSENSITIVE
    );

    public ParsedTransactionDto parse(String smsText) {
        if (smsText == null || smsText.isBlank()) {
            return new ParsedTransactionDto(null, null, TransactionType.UNKNOWN, null, smsText, false);
        }

        BigDecimal amount = extractAmount(smsText);
        String merchant = extractMerchant(smsText);
        TransactionType type = extractTransactionType(smsText);

        boolean success = amount != null && type != TransactionType.UNKNOWN;

        ParsedTransactionDto dto = new ParsedTransactionDto();
        dto.setAmount(amount);
        dto.setMerchant(merchant);
        dto.setTransactionType(type);
        dto.setRawMessage(smsText);
        dto.setParsedSuccessfully(success);
        return dto;
    }

    private BigDecimal extractAmount(String text) {
        Matcher matcher = AMOUNT_PATTERN.matcher(text);
        if (matcher.find()) {
            String rawAmount = matcher.group(1).replace(",", "");
            try {
                return new BigDecimal(rawAmount);
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return null;
    }

    private String extractMerchant(String text) {
        Matcher matcher = MERCHANT_VPA_PATTERN.matcher(text);
        if (matcher.find()) {
            String merchant = matcher.group(1).trim();
            // Clean UPI handles like "swiggy@ybl" -> "swiggy"
            if (merchant.contains("@")) {
                merchant = merchant.substring(0, merchant.indexOf("@"));
            }
            return capitalize(merchant.trim());
        }
        return "Unknown Merchant";
    }

    private TransactionType extractTransactionType(String text) {
        if (DEBIT_KEYWORDS.matcher(text).find()) return TransactionType.DEBIT;
        if (CREDIT_KEYWORDS.matcher(text).find()) return TransactionType.CREDIT;
        return TransactionType.UNKNOWN;
    }

    private String capitalize(String input) {
        if (input.isEmpty()) return input;
        return Character.toUpperCase(input.charAt(0)) + input.substring(1).toLowerCase();
    }
}