package com.radha.smartclaim.documentservice.dto;

import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateClaimRequestDto {
    private Long userId;
    private Long documentId;
    private BigDecimal amount;
}
