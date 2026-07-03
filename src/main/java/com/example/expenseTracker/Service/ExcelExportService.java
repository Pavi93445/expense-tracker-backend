package com.example.expenseTracker.service;

import com.example.expenseTracker.Entity.ExpenseEntity;
import com.example.expenseTracker.Entity.ProfileEntity;
import com.example.expenseTracker.repository.ExpenseRepository;
import com.example.expenseTracker.repository.ProfileRepository;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class ExcelExportService {

    private final ExpenseRepository expenseRepository;
    private final ProfileRepository profileRepository;

    public ExcelExportService(
            ExpenseRepository expenseRepository,
            ProfileRepository profileRepository
    ) {
        this.expenseRepository = expenseRepository;
        this.profileRepository = profileRepository;
    }

    public void exportExpenses(
            HttpServletResponse response
    ) throws IOException {

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        ProfileEntity user = profileRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found")
                );

        List<ExpenseEntity> expenses =
                expenseRepository.findByUserId(
                        user.getId()
                );

        XSSFWorkbook workbook =
                new XSSFWorkbook();

        Sheet sheet =
                workbook.createSheet("Expenses");

        Row headerRow = sheet.createRow(0);

        headerRow.createCell(0)
                .setCellValue("Title");

        headerRow.createCell(1)
                .setCellValue("Amount");

        headerRow.createCell(2)
                .setCellValue("Category");

        headerRow.createCell(3)
                .setCellValue("Date");

        headerRow.createCell(4)
                .setCellValue("Description");

        int rowNum = 1;

        for (ExpenseEntity expense : expenses) {

            Row row = sheet.createRow(rowNum++);

            row.createCell(0)
                    .setCellValue(expense.getTitle());

            row.createCell(1)
                    .setCellValue(expense.getAmount());

            row.createCell(2)
                    .setCellValue(expense.getCategory());

            row.createCell(3)
                    .setCellValue(
                            expense.getExpenseDate().toString()
                    );

            row.createCell(4)
                    .setCellValue(
                            expense.getDescription()
                    );
        }

        response.setContentType(
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
        );

        response.setHeader(
                "Content-Disposition",
                "attachment; filename=expenses.xlsx"
        );

        ServletOutputStream outputStream =
                response.getOutputStream();

        workbook.write(outputStream);

        workbook.close();

        outputStream.close();
    }
}