package com.radha.smartclaim.documentservice.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonProperty;

@Getter
@Setter
@NoArgsConstructor
public class ParsedDocumentDto {

    @JsonProperty("hospital_name")
    private String hospitalName;

    @JsonProperty("bill_date")
    private String billDate;

    @JsonProperty("patient_name")
    private String patientName;

    @JsonProperty("diagnosis")
    private String diagnosis;

    @JsonProperty("total_amount")
    private String totalAmount;

    @JsonProperty("patient_id")
    private String patientId;
}