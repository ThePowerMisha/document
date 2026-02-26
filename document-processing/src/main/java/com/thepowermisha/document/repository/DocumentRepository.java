package com.thepowermisha.document.repository;

import com.thepowermisha.document.entity.Author;
import com.thepowermisha.document.entity.Author_;
import com.thepowermisha.document.entity.Document;
import com.thepowermisha.document.entity.Document_;
import com.thepowermisha.document.request.DocumentSearchRequest;
import com.thepowermisha.document.type.DocumentStatus;
import jakarta.persistence.LockModeType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long>, JpaSpecificationExecutor<Document>{

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

    @Query("SELECT d FROM Document d WHERE d.id IN :ids")
    Page<Document> findByIdsWithPagination(List<Long> ids, Pageable pageable);

    @EntityGraph(attributePaths = "documentHistory")
    Optional<Document> findWithHistoryById(Long id);

    List<Document> findByStatus(DocumentStatus status);

    List<Document> findByAuthor(Author author);

    default Page<Document> filterDocument(DocumentSearchRequest request, Pageable pageable) {
        Specification<Document> specification = (root, query, builder) -> {
            Predicate conjunction = builder.conjunction();

            if (request.getStatus() != null) {
                conjunction = builder.and(builder.equal(root.get(Document_.STATUS), request.getStatus()));
            }
            if (request.getAuthorId() != null) {
                conjunction = builder.and(builder.equal(root.get(Document_.AUTHOR).get(Author_.ID), request.getAuthorId()));
            }
            if (request.getDateFrom() != null) {
                conjunction = builder.and(builder.greaterThan(root.get(Document_.CREATED_AT), request.getDateFrom()));
            }
            if (request.getDateTo() != null) {
                conjunction = builder.and(builder.lessThan(root.get(Document_.CREATED_AT), request.getDateTo()));
            }
//            root.fetch(Document_.AUTHOR, JoinType.LEFT);
//            root.fetch(Document_.DOCUMENT_HISTORY, JoinType.LEFT);
            return conjunction;
        };

        return findAll(specification, pageable);
    }
}
