package com.example.expenseTracker.service;

import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class CategoryClassifierService {

    // Order matters — first match wins
    private static final Map<String, String[]> CATEGORY_KEYWORDS = new LinkedHashMap<>();

    static {
        CATEGORY_KEYWORDS.put("Food", new String[]{"swiggy", "zomato", "food", "restaurant", "dominos", "kfc", "starbucks"});
        CATEGORY_KEYWORDS.put("Transport", new String[]{"uber", "ola", "rapido", "irctc", "redbus", "petrol", "fuel"});
        CATEGORY_KEYWORDS.put("Shopping", new String[]{"amazon", "flipkart", "myntra", "ajio", "meesho"});
        CATEGORY_KEYWORDS.put("Entertainment", new String[]{"netflix", "spotify", "hotstar", "bookmyshow", "prime"});
        CATEGORY_KEYWORDS.put("Utilities", new String[]{"electricity", "recharge", "airtel", "jio", "broadband", "gas"});
        CATEGORY_KEYWORDS.put("Groceries", new String[]{"bigbasket", "blinkit", "zepto", "dmart", "grocery"});
    }

    public String classify(String merchant) {
        if (merchant == null) return "Others";
        String lowerMerchant = merchant.toLowerCase();

        for (Map.Entry<String, String[]> entry : CATEGORY_KEYWORDS.entrySet()) {
            for (String keyword : entry.getValue()) {
                if (lowerMerchant.contains(keyword)) {
                    return entry.getKey();
                }
            }
        }
        return "Others";
    }
}