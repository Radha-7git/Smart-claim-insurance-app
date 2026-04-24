package com.radha.smartclaim.documentservice.service;

import java.io.File;
import org.springframework.stereotype.Service;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import org.springframework.beans.factory.annotation.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class OcrService {

    private static final Logger log = LoggerFactory.getLogger(OcrService.class);

    @Value("${tesseract.data-path:tessdata}")
    private String tessDataPath;

    public String extractText(String filePath) {
        try {
            ITesseract tesseract = new Tesseract();
            tesseract.setDatapath(tessDataPath);
            tesseract.setLanguage("eng");
            String text = tesseract.doOCR(new File(filePath));
            log.info("OCR completed");
            return text;
        } catch (Exception e) {
            log.error("Error extracting text: {}", e);
            throw new RuntimeException("Error extracting text from document", e);
        }
    }
}