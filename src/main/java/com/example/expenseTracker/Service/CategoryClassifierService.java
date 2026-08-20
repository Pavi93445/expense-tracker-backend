package com.example.expenseTracker.service;

import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class CategoryClassifierService {
    private static final Map<String, String[]> CATEGORY_KEYWORDS =
            new LinkedHashMap<>();

    static {

        CATEGORY_KEYWORDS.put(
                "Food",
                new String[]{
                        "swiggy",
                        "zomato",
                        "food",
                        "restaurant",
                        "dominos",
                        "kfc",
                        "starbucks"
                }
        );

        CATEGORY_KEYWORDS.put(
                "Transport",
                new String[]{
                        "uber",
                        "ola",
                        "rapido",
                        "irctc",
                        "redbus",
                        "petrol",
                        "fuel"
                }
        );

        CATEGORY_KEYWORDS.put(
                "Shopping",
                new String[]{
                        "amazon",
                        "flipkart",
                        "myntra",
                        "ajio",
                        "meesho"
                }
        );

        CATEGORY_KEYWORDS.put(
                "Entertainment",
                new String[]{
                        "netflix",
                        "spotify",
                        "hotstar",
                        "bookmyshow",
                        "prime"
                }
        );

        CATEGORY_KEYWORDS.put(
                "Utilities",
                new String[]{
                        "electricity",
                        "recharge",
                        "airtel",
                        "jio",
                        "broadband",
                        "gas"
                }
        );

        CATEGORY_KEYWORDS.put(
                "Groceries",
                new String[]{
                        "bigbasket",
                        "blinkit",
                        "zepto",
                        "dmart",
                        "grocery"
                }
        );
    }

    // 2. REGULAR EXPRESSIONS FOR MERCHANT EXTRACTION
    private static final Pattern DEBITED_TO_PATTERN =
            Pattern.compile(
                    "debited\\s+to\\s+(.+?)(?:\\s+for\\s+|\\s+on\\s+|\\s*$)",
                    Pattern.CASE_INSENSITIVE
            );

    private static final Pattern PAID_TO_PATTERN =
            Pattern.compile(
                    "paid\\s+to\\s+(.+?)(?:\\s+for\\s+|\\s+on\\s+|\\s*$)",
                    Pattern.CASE_INSENSITIVE
            );

    private static final Pattern AT_PATTERN =
            Pattern.compile(
                    "\\bat\\s+([A-Za-z0-9&._-]+)",
                    Pattern.CASE_INSENSITIVE
            );

    // 3. EXTRACT MERCHANT FROM SMS

    public String extractMerchant(String sms) {

        if (sms == null || sms.isBlank()) {
            return null;
        }

        // First pattern:
        // ₹700 debited to Zomato
        Matcher debitedMatcher =
                DEBITED_TO_PATTERN.matcher(sms);

        if (debitedMatcher.find()) {
            return cleanMerchant(debitedMatcher.group(1));
        }


        // Second pattern:
        // ₹700 paid to Zomato
        Matcher paidMatcher =
                PAID_TO_PATTERN.matcher(sms);

        if (paidMatcher.find()) {
            return cleanMerchant(paidMatcher.group(1));
        }


        // Third pattern:
        // Transaction of ₹700 at Zomato
        Matcher atMatcher =
                AT_PATTERN.matcher(sms);

        if (atMatcher.find()) {
            return cleanMerchant(atMatcher.group(1));
        }

        return null;
    }


    // =========================================================
    // 4. CLEAN MERCHANT NAME
    // =========================================================

    private String cleanMerchant(String merchant) {

        if (merchant == null) {
            return null;
        }

        merchant = merchant.trim();

        // Remove unwanted punctuation from beginning/end
        merchant = merchant.replaceAll(
                "^[\\s:,-]+|[\\s:,.!-]+$",
                ""
        );

        return merchant;
    }

    // 5. CLASSIFY MERCHANT

    public String classify(String merchant) {

        if (merchant == null || merchant.isBlank()) {
            return "Others";
        }

        String lowerMerchant =
                merchant.toLowerCase();

        for (Map.Entry<String, String[]> entry :
                CATEGORY_KEYWORDS.entrySet()) {

            String category = entry.getKey();

            String[] keywords = entry.getValue();

            for (String keyword : keywords) {

                if (lowerMerchant.contains(keyword)) {
                    return category;
                }
            }
        }

        return "Others";
    }
    // 6. EXTRACT MERCHANT + CLASSIFY SMS

    public String classifySms(String sms) {

        String merchant =
                extractMerchant(sms);

        return classify(merchant);
    }
}