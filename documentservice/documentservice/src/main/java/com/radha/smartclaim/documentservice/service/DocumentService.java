package com.radha.smartclaim.documentservice.service;

import org.springframework.stereotype.Service;
import com.radha.smartclaim.documentservice.repository.DocumentRepository;
import org.springframework.web.multipart.MultipartFile;
import com.radha.smartclaim.documentservice.entity.Document;
import java.io.IOException;
import java.io.File;
import com.radha.smartclaim.documentservice.enums.ParseStatus;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DocumentService {
    private final DocumentRepository documentRepository;
    private final DocumentProcessingService processingService;

    public DocumentService(DocumentRepository documentRepository, DocumentProcessingService processingService) {
        this.documentRepository = documentRepository;
        this.processingService = processingService;
    }

    public Document uploadDocument(MultipartFile file, long userId) throws IOException {
        if (file.isEmpty()) {
            throw new RuntimeException("File is empty");
        }
        String contentType = file.getContentType();
        if (contentType == null || !(contentType.equals("image/jpeg") || contentType.equals("image/png")
                || contentType.equals("application/pdf"))) {
            throw new RuntimeException("Invalid file type");
        }
        String originalName = file.getOriginalFilename();
        if (originalName == null) {
            throw new RuntimeException("Invalid file name");
        }
        String uploadDir = System.getProperty("user.dir") + File.separator + "uploads" + File.separator;
        String fileName = System.currentTimeMillis() + "_" + originalName;
        String path = uploadDir + fileName;

        File dir = new File(uploadDir);
        if (!dir.exists())
            dir.mkdirs();
        File dest = new File(path);
        file.transferTo(dest);

        Document doc = new Document();
        doc.setFilePath(path);
        doc.setFileName(originalName);
        doc.setFileType(contentType);
        doc.setUserId(userId);
        doc.setParseStatus(ParseStatus.PENDING);
        Document saved = documentRepository.save(doc);
        processingService.processDocument(saved);
        return saved;
    }

    public Document getById(Long id) {
        return documentRepository.findById(id).orElseThrow(() -> new RuntimeException("Document not found"));
    }
}
