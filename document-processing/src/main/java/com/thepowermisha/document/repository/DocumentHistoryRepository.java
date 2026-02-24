package com.thepowermisha.document.repository;

import com.thepowermisha.document.entity.DocumentHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DocumentHistoryRepository extends JpaRepository<DocumentHistory, UUID> {
}
