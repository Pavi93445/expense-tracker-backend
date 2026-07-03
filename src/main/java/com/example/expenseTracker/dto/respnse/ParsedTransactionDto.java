package com.example.expenseTracker.dto.respnse;


import com.example.expenseTracker.enums.TransactionType;

import java.math.BigDecimal;

public class ParsedTransactionDto {

    private BigDecimal amount;
    private String merchant;
    private TransactionType transactionType;
    private String category;
    private String rawMessage;
    private boolean parsedSuccessfully;

    // Constructors
    public ParsedTransactionDto() {}

    public ParsedTransactionDto(BigDecimal amount, String merchant,
                                TransactionType transactionType, String category,
                                String rawMessage, boolean parsedSuccessfully) {
        this.amount = amount;
        this.merchant = merchant;
        this.transactionType = transactionType;
        this.category = category;
        this.rawMessage = rawMessage;
        this.parsedSuccessfully = parsedSuccessfully;
    }

    // getters and setters
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public String getMerchant() { return merchant; }
    public void setMerchant(String merchant) { this.merchant = merchant; }
    public TransactionType getTransactionType() { return transactionType; }
    public void setTransactionType(TransactionType transactionType) { this.transactionType = transactionType; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getRawMessage() { return rawMessage; }
    public void setRawMessage(String rawMessage) { this.rawMessage = rawMessage; }
    public boolean isParsedSuccessfully() { return parsedSuccessfully; }
    public void setParsedSuccessfully(boolean parsedSuccessfully) { this.parsedSuccessfully = parsedSuccessfully; }
}