package com.example.expenseTracker.Ocr.Dto;

import java.math.BigDecimal;

public class ReceiptScanResponseDto {

    private BigDecimal amount;
    private String merchant;
    private String category;
    private String rawExtractedText;
    private boolean parsedSuccessfully;

    public ReceiptScanResponseDto() {}

    public ReceiptScanResponseDto(BigDecimal amount, String merchant, String category,
                                  String rawExtractedText, boolean parsedSuccessfully) {
        this.amount = amount;
        this.merchant = merchant;
        this.category = category;
        this.rawExtractedText = rawExtractedText;
        this.parsedSuccessfully = parsedSuccessfully;
    }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public String getMerchant() { return merchant; }
    public void setMerchant(String merchant) { this.merchant = merchant; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getRawExtractedText() { return rawExtractedText; }
    public void setRawExtractedText(String rawExtractedText) { this.rawExtractedText = rawExtractedText; }
    public boolean isParsedSuccessfully() { return parsedSuccessfully; }
    public void setParsedSuccessfully(boolean parsedSuccessfully) { this.parsedSuccessfully = parsedSuccessfully; }
}