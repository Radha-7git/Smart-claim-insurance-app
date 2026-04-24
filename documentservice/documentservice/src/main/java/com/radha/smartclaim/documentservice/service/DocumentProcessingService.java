package com.radha.smartclaim.documentservice.service;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.scheduling.annotation.Async;
import com.radha.smartclaim.documentservice.entity.Document;
import com.radha.smartclaim.documentservice.enums.ParseStatus;
import com.radha.smartclaim.documentservice.dto.ParsedDocumentDto;
import com.radha.smartclaim.documentservice.repository.DocumentRepository;
import org.springframework.web.client.RestTemplate;
import com.radha.smartclaim.documentservice.dto.CreateClaimRequestDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class DocumentProcessingService {
    private final DocumentRepository documentRepository;
    private final OcrService ocrService;
    private final AIParsingService aiParsingService;
    private final RestTemplate restTemplate;
    private static final Logger log = LoggerFactory.getLogger(DocumentProcessingService.class);

    public DocumentProcessingService(DocumentRepository documentRepository, OcrService ocrService,
            AIParsingService aiParsingService, RestTemplate restTemplate) {
        this.documentRepository = documentRepository;
        this.ocrService = ocrService;
        this.aiParsingService = aiParsingService;
        this.restTemplate = restTemplate;

    }

    @Async
    public void processDocument(Document document) {
        log.info("Starting processing for document {}", document.getId());
        try {
            String ocrText = ocrService.extractText(document.getFilePath());
            log.info("OCR completed for document {}", document.getId());
            document.setOcrRawText(ocrText);
            String json = aiParsingService.parseText(ocrText);
            ParsedDocumentDto parsedDoc = aiParsingService.convertJsonToDto(json);
            log.info("AI parsing completed for document {}", document.getId());
            document.setHospitalName(parsedDoc.getHospitalName());
            document.setPatientName(parsedDoc.getPatientName());
            document.setDiagnosis(parsedDoc.getDiagnosis());
            if (parsedDoc.getTotalAmount() != null) {
                try {
                    String cleaned = parsedDoc.getTotalAmount()
                            .replace(",", "")
                            .trim();
                    document.setTotalAmount(new BigDecimal(cleaned));
                } catch (Exception e) {
                    document.setParseStatus(ParseStatus.FAILED);
                }
            }
            if (parsedDoc.getBillDate() != null) {
                try {
                    document.setBillDate(LocalDate.parse(parsedDoc.getBillDate()));
                } catch (Exception e) {
                    document.setParseStatus(ParseStatus.FAILED);
                }
            }
            document.setParseStatus(ParseStatus.SUCCESS);
            if (document.getParseStatus() == ParseStatus.SUCCESS) {
                log.info("Calling claim service for document {}", document.getId());
                CreateClaimRequestDto request = new CreateClaimRequestDto();
                request.setUserId(document.getUserId());
                request.setDocumentId(document.getId());
                request.setAmount(document.getTotalAmount());

                restTemplate.postForObject(
                        "http://localhost:8083/api/claims/create",
                        request,
                        Object.class);
            }
            log.info("Claim created for document {}", document.getId());
        } catch (Exception e) {
            log.error("Processing failed for document {}: {}", document.getId(), e.getMessage());
            document.setParseStatus(ParseStatus.FAILED);
        }
        documentRepository.save(document);
    }
}
