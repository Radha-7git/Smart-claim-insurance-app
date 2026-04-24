package com.radha.smartclaim.claimservice.entity;

import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import com.radha.smartclaim.claimservice.enums.ClaimsStatus;
import java.math.BigDecimal;
import jakarta.persistence.EnumType;

@Entity
@Table(name = "claims")
@Getter
@Setter
public class Claim {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long documentId;
    private Long userId;
    private BigDecimal claimAmount;
    @Enumerated(EnumType.STRING)
    private ClaimsStatus claimStatus;
    private String reason;
}
