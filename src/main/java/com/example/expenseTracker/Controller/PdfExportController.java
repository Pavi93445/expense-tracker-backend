package com.example.expenseTracker.controller;

import com.example.expenseTracker.service.PdfExportService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/pdf")
public class PdfExportController {

    private final PdfExportService pdfExportService;

    public PdfExportController(
            PdfExportService pdfExportService
    ) {
        this.pdfExportService = pdfExportService;
    }

    @GetMapping("/expenses")
    public void exportPdf(
            HttpServletResponse response
    ) throws IOException {

        pdfExportService.exportPdf(response);
    }
}