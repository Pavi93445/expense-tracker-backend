# Expense Tracker Backend API

A professional Spring Boot backend application for managing personal expenses, budgets, reports, notifications, and analytics.

---

# Features

## Authentication & Security
- JWT Authentication
- Login & Register
- Email Verification
- Password Change
- Spring Security

## Expense Management
- Add Expense
- Update Expense
- Delete Expense
- Get All Expenses
- Search Expenses
- Filter By Category
- Filter By Date
- Pagination

## Budget System
- Monthly Budget Limit
- Budget Tracking
- Remaining Budget Calculation

## Reports & Analytics
- Monthly Expense Report
- Category-wise Analytics
- Expense Summary

## Notifications
- In-App Notifications
- Budget Warning Alerts

## Export Features
- Excel Export
- PDF Export

## Scheduler
- Automatic Monthly Reports
- Scheduled Email Notifications

---

# Tech Stack

- Java 17
- Spring Boot
- Spring Security
- JWT
- Spring Data JPA
- MySQL
- Maven
- Swagger
- Apache POI
- iText PDF

---

# API Documentation

Swagger UI:

```bash
http://localhost:8080/swagger-ui/index.html
```

---

# Project Structure

```text
src/main/java/com/example/expenseTracker
│
├── Config
├── Controller
├── Service
├── Repository
├── Entity
├── Dto
├── Exception
```

---

# Database

MySQL Database used.

Tables:
- users
- expenses
- budgets
- notifications
- goals
- recurring_expenses

---

# Security

- JWT Token Authentication
- Stateless Session
- Protected APIs
- Password Encryption

---

# Sample API

## Login

POST `/auth/login`

```json
{
  "email": "user@gmail.com",
  "password": "password123"
}
```

---

# Run Project

## Clone Project

```bash
git clone YOUR_GITHUB_REPO_LINK
```

## Open Project

```bash
cd expenseTracker
```

## Run

```bash
mvn spring-boot:run
```

---

# Future Improvements

- Docker Deployment
- AWS Deployment
- React Frontend
- Mobile App Integration

---

# Author

Pavithran