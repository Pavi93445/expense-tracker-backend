package com.example.expenseTracker.Ocr.Service;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ReceiptParserService {

    // Matches lines like: "TOTAL: 450.00", "Total Rs.450", "Grand Total 450.00", "Amount Due: 450"
    private static final Pattern TOTAL_AMOUNT_PATTERN = Pattern.compile(
            "(?:grand\\s*)?total\\s*(?:due)?\\s*[:\\-]?\\s*(?:rs\\.?|inr)?\\s*([0-9]+(?:,[0-9]{2,3})*(?:\\.[0-9]{1,2})?)",
            Pattern.CASE_INSENSITIVE
    );

    // Fallback: any currency-formatted number in the text, if no "total" line found
    private static final Pattern GENERIC_AMOUNT_PATTERN = Pattern.compile(
            "(?:rs\\.?|inr|₹)\\s?([0-9]+(?:,[0-9]{2,3})*(?:\\.[0-9]{1,2})?)",
            Pattern.CASE_INSENSITIVE
    );

    public BigDecimal extractAmount(String receiptText) {
        if (receiptText == null || receiptText.isBlank()) {
            return null;
        }

        Matcher totalMatcher = TOTAL_AMOUNT_PATTERN.matcher(receiptText);
        if (totalMatcher.find()) {
            return parseAmount(totalMatcher.group(1));
        }

        // Fallback: pick the largest currency amount found (usually the total on a receipt)
        Matcher genericMatcher = GENERIC_AMOUNT_PATTERN.matcher(receiptText);
        BigDecimal largest = null;
        while (genericMatcher.find()) {
            BigDecimal candidate = parseAmount(genericMatcher.group(1));
            if (candidate != null && (largest == null || candidate.compareTo(largest) > 0)) {
                largest = candidate;
            }
        }
        return largest;
    }

    // Common label prefixes that precede the actual merchant name on a receipt line
    private static final Pattern LABEL_PREFIX_PATTERN = Pattern.compile(
            "^(?:store|shop|merchant|vendor|business)\\s*[:\\-]\\s*",
            Pattern.CASE_INSENSITIVE
    );

    public String extractMerchant(String receiptText) {
        if (receiptText == null || receiptText.isBlank()) {
            return "Unknown Merchant";
        }

        // Heuristic: the merchant name is usually the first non-empty line of a receipt
        String[] lines = receiptText.split("\\r?\\n");
        for (String line : lines) {
            String trimmed = line.trim();
            // Skip empty lines and lines that are mostly numbers/symbols (like addresses, phone numbers)
            if (!trimmed.isEmpty() && trimmed.matches(".*[a-zA-Z]{3,}.*")) {
                return cleanMerchantName(trimmed);
            }
        }
        return "Unknown Merchant";
    }

    private String cleanMerchantName(String rawLine) {
        // Strip common "Store :", "Shop -", "Merchant:" style label prefixes
        String cleaned = LABEL_PREFIX_PATTERN.matcher(rawLine).replaceFirst("");
        // Strip trailing punctuation like a stray comma or colon
        cleaned = cleaned.replaceAll("[,:;\\s]+$", "");
        return cleaned.trim();
    }

    private BigDecimal parseAmount(String rawAmount) {
        try {
            return new BigDecimal(rawAmount.replace(",", ""));
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
