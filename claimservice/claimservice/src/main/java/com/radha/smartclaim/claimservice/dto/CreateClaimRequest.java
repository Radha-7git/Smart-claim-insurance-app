package com.radha.smartclaim.claimservice.dto;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateClaimRequest {
    private Long userId;
    private Long documentId;
    private BigDecimal amount;
}
