package com.example.expenseTracker.controller;

import com.example.expenseTracker.service.ExcelExportService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/export")
public class ExcelExportController {

    private final ExcelExportService excelExportService;

    public ExcelExportController(
            ExcelExportService excelExportService
    ) {
        this.excelExportService = excelExportService;
    }

    @GetMapping("/expenses")
    public void exportExpenses(
            HttpServletResponse response
    ) throws IOException {

        excelExportService.exportExpenses(response);
    }
}