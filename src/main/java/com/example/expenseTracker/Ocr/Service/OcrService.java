package com.example.expenseTracker.Ocr.Service;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@Service
public class OcrService {

    private final Tesseract tesseract;

    public OcrService() {
        tesseract = new Tesseract();
        tesseract.setDatapath("C:/Users/pavit/AppData/Local/Programs/Tesseract-OCR/tessdata");
        tesseract.setLanguage("eng");

        // Use LSTM-only OCR engine mode - generally more accurate on real-world photos
        tesseract.setOcrEngineMode(1);
        // Assume a single uniform block of text (better for receipts than default mode)
        tesseract.setPageSegMode(6);
    }

    public String extractText(MultipartFile file) throws IOException, TesseractException {
        File tempFile = File.createTempFile("receipt-", "-" + file.getOriginalFilename());
        Files.write(tempFile.toPath(), file.getBytes());

        try {
            BufferedImage original = ImageIO.read(tempFile);
            if (original == null) {
                throw new IOException("Could not read image file — unsupported format or corrupted file");
            }

            BufferedImage processed = preprocessImage(original);

            File processedFile = File.createTempFile("receipt-processed-", ".png");
            ImageIO.write(processed, "png", processedFile);

            try {
                return tesseract.doOCR(processedFile);
            } finally {
                processedFile.delete();
            }

        } finally {
            tempFile.delete();
        }
    }

    /**
     * Preprocessing pipeline to improve OCR accuracy:
     * 1. Convert to grayscale (removes color noise/patterns)
     * 2. Scale up if the image is small (Tesseract works better on higher resolution)
     * 3. Apply simple binarization (threshold) to increase text/background contrast
     */
    private BufferedImage preprocessImage(BufferedImage original) {
        BufferedImage scaled = scaleIfNeeded(original);
        return toGrayscale(scaled);
        // Binarization (applyThreshold) removed — it worsened results on
        // receipts with decorative/watermark background patterns by turning
        // subtle texture into sharp false "text" shapes. Grayscale + scaling
        // alone is a safer default. Re-enable applyThreshold() selectively
        // later if testing shows it helps on plain, low-contrast receipts.
    }

    private BufferedImage scaleIfNeeded(BufferedImage image) {
        int minDimension = 1500; // target minimum width/height for good OCR accuracy
        int width = image.getWidth();
        int height = image.getHeight();

        if (width >= minDimension && height >= minDimension) {
            return image;
        }

        double scaleFactor = Math.max((double) minDimension / width, (double) minDimension / height);
        int newWidth = (int) (width * scaleFactor);
        int newHeight = (int) (height * scaleFactor);

        BufferedImage scaledImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
        scaledImage.createGraphics().drawImage(
                image.getScaledInstance(newWidth, newHeight, java.awt.Image.SCALE_SMOOTH),
                0, 0, null
        );
        return scaledImage;
    }

    private BufferedImage toGrayscale(BufferedImage image) {
        BufferedImage grayImage = new BufferedImage(
                image.getWidth(), image.getHeight(), BufferedImage.TYPE_BYTE_GRAY
        );
        grayImage.getGraphics().drawImage(image, 0, 0, null);
        return grayImage;
    }

    private BufferedImage applyThreshold(BufferedImage grayImage) {
        int width = grayImage.getWidth();
        int height = grayImage.getHeight();
        BufferedImage binaryImage = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);

        int threshold = 150; // pixels darker than this become black, lighter become white

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int gray = grayImage.getRGB(x, y) & 0xFF;
                int newColor = (gray < threshold) ? 0x000000 : 0xFFFFFF;
                binaryImage.setRGB(x, y, newColor);
            }
        }
        return binaryImage;
    }
}
