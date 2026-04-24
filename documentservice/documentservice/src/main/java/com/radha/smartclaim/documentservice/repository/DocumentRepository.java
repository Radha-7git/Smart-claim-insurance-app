package com.radha.smartclaim.documentservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.radha.smartclaim.documentservice.entity.Document;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {

}
