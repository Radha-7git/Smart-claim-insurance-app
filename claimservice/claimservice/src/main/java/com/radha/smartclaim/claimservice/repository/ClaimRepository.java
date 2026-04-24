package com.radha.smartclaim.claimservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import com.radha.smartclaim.claimservice.entity.Claim;

@Repository
public interface ClaimRepository extends JpaRepository<Claim, Long> {
    Optional<Claim> findByDocumentId(Long documentId);
}
