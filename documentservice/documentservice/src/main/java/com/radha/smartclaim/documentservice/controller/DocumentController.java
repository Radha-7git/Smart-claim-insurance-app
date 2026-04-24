package com.radha.smartclaim.documentservice.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import com.radha.smartclaim.documentservice.service.DocumentService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.radha.smartclaim.documentservice.dto.DocumentResponseDto;
import com.radha.smartclaim.documentservice.entity.Document;
import java.io.IOException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/api/documents")
public class DocumentController {
    private final DocumentService documentService;

    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @PostMapping("/upload")
    public DocumentResponseDto upload(@RequestParam("file") MultipartFile file) throws IOException {
        Long userId = 1L;
        Document document = documentService.uploadDocument(file, userId);
        return mapDocumentToResponseDto(document);
    }

    private DocumentResponseDto mapDocumentToResponseDto(Document document) {
        DocumentResponseDto documentResponseDto = new DocumentResponseDto();
        documentResponseDto.setId(document.getId());
        documentResponseDto.setFileName(document.getFileName());
        documentResponseDto.setHospitalName(document.getHospitalName());
        documentResponseDto.setTotalAmount(document.getTotalAmount());
        documentResponseDto.setParseStatus(document.getParseStatus().name());
        return documentResponseDto;
    }

    @GetMapping("/{id}")
    public DocumentResponseDto getById(@PathVariable Long id) {
        Document document = documentService.getById(id);
        return mapDocumentToResponseDto(document);
    }

}