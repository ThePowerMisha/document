package com.thepowermisha.document.repository;

import com.thepowermisha.document.entity.Author;
import com.thepowermisha.document.entity.Document;
import com.thepowermisha.document.type.DocumentStatus;
import jakarta.persistence.LockModeType;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
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

    @EntityGraph(attributePaths = "documentHistory")
    Page<Document> findByIdIn(List<Long> ids, Pageable pageable);

    @EntityGraph(attributePaths = "documentHistory")
    Optional<Document> findWithHistoryById(Long id);

    List<Document> findByStatus(DocumentStatus status);

    List<Document> findByAuthor(Author author);
}
