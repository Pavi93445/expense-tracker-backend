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

        String subject = "🚀 Verify Your Email - Expense Tracker";

        String verificationLink =
                "http://localhost:8080/auth/verify?token=" + token;

        String body =
                "Hi 👋,\n\n" +
                        "Welcome to *Expense Tracker* 💰!\n\n" +

                        "You're just one step away from getting started.\n" +
                        "Please verify your email address by clicking the link below:\n\n" +

                        verificationLink + "\n\n" +

                        "⏳ Note: This link will expire in 30 minutes.\n\n" +

                        "Once verified, you can:\n" +
                        "✔ Track your expenses easily\n" +
                        "✔ Manage your finances smarter\n" +
                        "✔ Stay in control of your money 💡\n\n" +

                        "If you didn’t create this account, you can safely ignore this email.\n\n" +

                        "Happy Saving! 💸\n" +
                        "— Expense Tracker Team";

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("moneymateofficiall@gmail.com");
        message.setTo(toEmail);
        message.setSubject(subject);
        message.setText(body);

        mailSender.send(message);
    }
}