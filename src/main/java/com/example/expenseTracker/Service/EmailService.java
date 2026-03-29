package com.example.expenseTracker.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendVerificationEmail(String toEmail, String token) {

        String subject = "Verify your Email - Expense Tracker";

        String verificationLink =
                "http://localhost:8080/auth/verify?token=" + token;

        String body =
                "Hi,\n\n" +
                        "Please click the link below to verify your email:\n" +
                        verificationLink +
                        "\n\nThanks,\nExpense Tracker Team";

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("moneymateofficiall@gmail.com");
        message.setTo(toEmail);
        message.setSubject(subject);
        message.setText(body);

        mailSender.send(message);
    }
}