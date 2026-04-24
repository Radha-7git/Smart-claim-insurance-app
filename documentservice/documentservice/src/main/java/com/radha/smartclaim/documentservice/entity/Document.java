package com.radha.smartclaim.documentservice.entity;

import com.radha.smartclaim.documentservice.enums.ParseStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.EnumType;
import java.time.LocalDate;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import java.math.BigDecimal;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    private String fileName;
    private String filePath;
    private String fileType;
    @Column(columnDefinition = "LONGTEXT")
    private String ocrRawText;
    private String hospitalName;
    private LocalDate billDate;
    @Column(columnDefinition = "LONGTEXT")
    private String diagnosis;
    private BigDecimal totalAmount;
    private String patientName;
    private String patientId;
    @Enumerated(EnumType.STRING)
    private ParseStatus parseStatus;
}
