package com.radha.smartclaim.claimservice.service;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.radha.smartclaim.claimservice.dto.CreateClaimRequest;
import com.radha.smartclaim.claimservice.repository.ClaimRepository;
import com.radha.smartclaim.claimservice.entity.Claim;
import com.radha.smartclaim.claimservice.enums.ClaimsStatus;
import java.util.Optional;

@Service
public class ClaimService {
    private final ClaimRepository claimRepository;

    public ClaimService(ClaimRepository claimRepository) {
        this.claimRepository = claimRepository;
    }

    public Claim createClaim(CreateClaimRequest request) {
        Claim claim = new Claim();
        claim.setUserId(request.getUserId());
        claim.setDocumentId(request.getDocumentId());
        claim.setClaimAmount(request.getAmount());
        if (request.getAmount() == null) {
            claim.setClaimStatus(ClaimsStatus.REJECTED);
            claim.setReason("Amount Missing");
        } else if (request.getAmount().compareTo(new BigDecimal(50000)) <= 0) {
            claim.setClaimStatus(ClaimsStatus.APPROVED);
            claim.setReason("Amount within auto - approval limit");
        } else {
            claim.setClaimStatus(ClaimsStatus.PENDING);
            claim.setReason("Needs Manual Approval");
        }
        return claimRepository.save(claim);
    }

    public Optional<Claim> getClaimByDocumentId(Long documentId) {
        return claimRepository.findByDocumentId(documentId);
    }
}
