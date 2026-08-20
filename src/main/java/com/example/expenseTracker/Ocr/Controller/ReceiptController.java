package com.example.expenseTracker.Ocr.Controller;

import com.example.expenseTracker.Ocr.Dto.ReceiptScanResponseDto;
import com.example.expenseTracker.Ocr.Service.OcrService;
import com.example.expenseTracker.Ocr.Service.ReceiptParserService;

import com.example.expenseTracker.service.CategoryClassifierService;
import net.sourceforge.tess4j.TesseractException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;

@RestController
@RequestMapping("/api/receipt")
public class ReceiptController {

    private final OcrService ocrService;
    private final ReceiptParserService receiptParserService;
    private final CategoryClassifierService categoryClassifierService;

    public ReceiptController(OcrService ocrService,
                             ReceiptParserService receiptParserService,
                             CategoryClassifierService categoryClassifierService) {
        this.ocrService = ocrService;
        this.receiptParserService = receiptParserService;
        this.categoryClassifierService = categoryClassifierService;
    }

    @PostMapping(value = "/scan", consumes = "multipart/form-data")
    public ResponseEntity<ReceiptScanResponseDto> scanReceipt(@RequestParam("file") MultipartFile file) {
        try {
            String rawText = ocrService.extractText(file);

            BigDecimal amount = receiptParserService.extractAmount(rawText);
            String merchant = receiptParserService.extractMerchant(rawText);
            String category = categoryClassifierService.classify(merchant);

            boolean success = amount != null;

            ReceiptScanResponseDto response = new ReceiptScanResponseDto(
                    amount, merchant, category, rawText, success
            );

            return ResponseEntity.ok(response);

        } catch (IOException | TesseractException e) {
            ReceiptScanResponseDto errorResponse = new ReceiptScanResponseDto(
                    null, null, null, "OCR processing failed: " + e.getMessage(), false
            );
            return ResponseEntity.ok(errorResponse);
        }
    }
}