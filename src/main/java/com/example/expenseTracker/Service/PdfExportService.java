package com.example.expenseTracker.service;

import com.example.expenseTracker.Entity.ExpenseEntity;
import com.example.expenseTracker.Entity.ProfileEntity;
import com.example.expenseTracker.repository.ExpenseRepository;
import com.example.expenseTracker.repository.ProfileRepository;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class PdfExportService {

    private final ExpenseRepository expenseRepository;
    private final ProfileRepository profileRepository;

    public PdfExportService(
            ExpenseRepository expenseRepository,
            ProfileRepository profileRepository
    ) {
        this.expenseRepository = expenseRepository;
        this.profileRepository = profileRepository;
    }

    public void exportPdf(
            HttpServletResponse response
    ) throws IOException {

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        ProfileEntity user = profileRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        List<ExpenseEntity> expenses =
                expenseRepository.findByUserId(
                        user.getId()
                );

        response.setContentType("application/pdf");

        response.setHeader(
                "Content-Disposition",
                "attachment; filename=expenses.pdf"
        );

        PdfWriter writer =
                new PdfWriter(response.getOutputStream());

        PdfDocument pdfDocument =
                new PdfDocument(writer);

        Document document =
                new Document(pdfDocument);

        document.add(
                new Paragraph("Expense Report")
        );

        document.add(
                new Paragraph("---------------------------")
        );

        for (ExpenseEntity expense : expenses) {

            document.add(
                    new Paragraph(
                            "Title : " + expense.getTitle()
                    )
            );

            document.add(
                    new Paragraph(
                            "Amount : " + expense.getAmount()
                    )
            );

            document.add(
                    new Paragraph(
                            "Category : " + expense.getCategory()
                    )
            );

            document.add(
                    new Paragraph(
                            "Date : " + expense.getExpenseDate()
                    )
            );

            document.add(
                    new Paragraph(
                            "Description : "
                                    + expense.getDescription()
                    )
            );

            document.add(
                    new Paragraph("---------------------------")
            );
        }

        document.close();
    }
}