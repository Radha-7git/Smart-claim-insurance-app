package com.radha.smartclaim.claimservice.dto;

import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

@Getter
@Setter
public class ClaimResponse {
    private long documentId;
    private String claimStatus;
    private String reason;
    private BigDecimal claimAmount;
}
