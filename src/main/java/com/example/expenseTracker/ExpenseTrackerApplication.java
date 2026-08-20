package com.example.expenseTracker;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ExpenseTrackerApplication {

	public static void main(String[] args) {
		loadEnv();
		SpringApplication.run(ExpenseTrackerApplication.class, args);
	}

	private static void loadEnv() {
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

	private static void setIfPresent(String key, Dotenv dotenv) {
		String value = dotenv.get(key);

		if (value != null && !value.isBlank()) {
			System.setProperty(key, value);
			System.out.println("Loaded: " + key + " ✅");
		} else {
			System.out.println("❌ Missing ENV: " + key);
		}
	}
}