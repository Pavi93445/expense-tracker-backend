package com.example.expenseTracker.Config;

import io.github.cdimascio.dotenv.Dotenv;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EnvConfig {

    @PostConstruct
    public void init() {

        // Load .env file from project root
        Dotenv dotenv = Dotenv.configure()
                .directory("C:/Users/pavit/Downloads/expenseTracker/expenseTracker")
                .ignoreIfMalformed()
                .ignoreIfMissing()
                .load();

        // Load variables safely
        setIfPresent("MAIL_USERNAME", dotenv);
        setIfPresent("MAIL_PASSWORD", dotenv);
        setIfPresent("DB_PASSWORD", dotenv);
        setIfPresent("JWT_SECRET", dotenv);

        // Debug (optional - remove later)
        System.out.println("ENV Loaded ✅");
    }

    private void setIfPresent(String key, Dotenv dotenv) {
        String value = dotenv.get(key);

        if (value != null && !value.isBlank()) {
            System.setProperty(key, value);
            System.out.println("Loaded: " + key + " ✅");
        } else {
            System.out.println("❌ Missing ENV: " + key);
        }
    }
}