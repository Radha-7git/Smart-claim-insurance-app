package com.radha.smartclaim.claimservice.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.radha.smartclaim.claimservice.service.ClaimService;
import org.springframework.web.bind.annotation.PostMapping;
import com.radha.smartclaim.claimservice.entity.Claim;
import com.radha.smartclaim.claimservice.dto.CreateClaimRequest;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import com.radha.smartclaim.claimservice.dto.ClaimResponse;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/claims")
public class ClaimController {
    private final ClaimService claimService;
    private final Logger log = LoggerFactory.getLogger(ClaimController.class);

    public ClaimController(ClaimService claimService) {
        this.claimService = claimService;
    }

    @PostMapping("/create")
    public Claim createClaim(@RequestBody CreateClaimRequest request) {
        log.info("Received claim creation request for document {}", request.getDocumentId());
        return claimService.createClaim(request);
    }

    @GetMapping("/document/{documentId}")
    public ClaimResponse getClaimByDocumentId(@PathVariable Long documentId) {
        return mapToClaimResponse(claimService.getClaimByDocumentId(documentId));
    }

    private ClaimResponse mapToClaimResponse(Optional<Claim> claim) {
        ClaimResponse response = new ClaimResponse();
        response.setDocumentId(claim.get().getDocumentId());
        response.setClaimStatus(claim.get().getClaimStatus().name());
        response.setReason(claim.get().getReason());
        response.setClaimAmount(claim.get().getClaimAmount());
        return response;
    }
}
