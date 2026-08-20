package com.example.expenseTracker.config;

import io.github.cdimascio.dotenv.Dotenv;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EnvConfig {

    @PostConstruct
    public void init() {

        // Loads .env locally; in production, real environment variables take precedence
        Dotenv dotenv = Dotenv.configure()
                .ignoreIfMalformed()
                .ignoreIfMissing()
                .load();

        setIfPresent("DB_URL", dotenv);
        setIfPresent("DB_USERNAME", dotenv);
        setIfPresent("DB_PASSWORD", dotenv);
        setIfPresent("MAIL_USERNAME", dotenv);
        setIfPresent("MAIL_PASSWORD", dotenv);
        setIfPresent("JWT_SECRET", dotenv);

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