package com.radha.smartclaim.documentservice.dto;

import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

@Getter
@Setter
public class DocumentResponseDto {
    private Long id;
    private String fileName;
    private String hospitalName;
    private BigDecimal totalAmount;
    private String parseStatus;
}
