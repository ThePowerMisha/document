package com.thepowermisha.document.repository;

import com.thepowermisha.document.entity.Document;
import com.thepowermisha.document.type.DocumentStatus;
import jakarta.persistence.LockModeType;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {

    @Query("SELECT d FROM Document d WHERE d.id in :ids")
    @Lock(value = LockModeType.PESSIMISTIC_WRITE)
    List<Document> getAll(List<Long> ids);

    @Modifying
    @Query("""
       update Document d
       set d.status = :status,
           d.updatedAt = CURRENT_TIMESTAMP
       where d.id = :id
       """)
    void updateStatus(Long id, DocumentStatus status);
}
