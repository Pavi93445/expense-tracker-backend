package com.example.expenseTracker.dto.request;

import jakarta.validation.constraints.NotBlank;

public class SmsParseRequestDto {

    @NotBlank(message = "SMS text must not be empty")
    private String smsText;

    private String sender; // optional: "GPay", "Paytm", "HDFCBK" etc.

    // getters and setters
    public String getSmsText() { return smsText; }
    public void setSmsText(String smsText) { this.smsText = smsText; }
    public String getSender() { return sender; }
    public void setSender(String sender) { this.sender = sender; }
}