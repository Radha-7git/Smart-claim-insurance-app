package com.radha.smartclaim.documentservice.service;

import java.io.File;
import org.springframework.stereotype.Service;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import org.springframework.beans.factory.annotation.Value;

@Service
public class OcrService {

    @Value("${tesseract.data-path:tessdata}")
    private String tessDataPath;

    public String extractText(String filePath) {
        try {
            ITesseract tesseract = new Tesseract();
            tesseract.setDatapath(tessDataPath);
            tesseract.setLanguage("eng");
            return tesseract.doOCR(new File(filePath));
        } catch (Exception e) {
            throw new RuntimeException("Error extracting text from document", e);
        }
    }
}