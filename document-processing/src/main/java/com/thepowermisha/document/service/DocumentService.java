package com.thepowermisha.document.service;

import com.thepowermisha.document.dto.DocumentDto;
import com.thepowermisha.document.dto.DocumentProcessedStatusDto;
import com.thepowermisha.document.entity.Author;
import com.thepowermisha.document.entity.Document;
import com.thepowermisha.document.exception.DocumentProcessingException;
import com.thepowermisha.document.exception.DocumentRegisterException;
import com.thepowermisha.document.mapper.DocumentMapper;
import com.thepowermisha.document.repository.AuthorRepository;
import com.thepowermisha.document.repository.DocumentRepository;
import com.thepowermisha.document.request.DocumentSearchRequest;
import com.thepowermisha.document.request.DocumentSortingRequest;
import com.thepowermisha.document.type.DocumentResultStatus;
import com.thepowermisha.document.type.DocumentStatus;
import com.thepowermisha.document.util.DocumentNumberGenerator;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.*;
import java.util.concurrent.*;


@Service
@RequiredArgsConstructor
public class DocumentService {
    private final DocumentRepository documentRepository;

    private final DocumentTransactionalService documentTransactionalService;

    private final AuthorRepository authorRepository;

    private final DocumentMapper documentMapper;

    /**
     * @param id Document id
     * @return return Document by id
     */
    public DocumentDto getDocument(Long id) {
        return documentMapper.toDto(documentRepository.findWithHistoryById(id)
                .orElseThrow(() -> new EntityNotFoundException("Document not found")));
    }

    public List<DocumentDto> getDocumentsList(DocumentSortingRequest request) {
        Pageable pageable = PageRequest.of(
                request.getPage(),
                request.getSize(),
                Sort.by(Sort.Direction.fromString(request.getDirection().getFieldName()), request.getSortBy().getFieldName())
        );
        return documentRepository.findAll(pageable)
                .stream()
                .map(documentMapper::toDto)
                .toList();
    }

    public List<DocumentDto> getDocumentsListByIds(List<Long> ids, DocumentSortingRequest request) {

        Pageable pageable = PageRequest.of(
                request.getPage(),
                request.getSize(),
                Sort.by(Sort.Direction.fromString(request.getDirection().getFieldName()), request.getSortBy().getFieldName())
        );

        return documentRepository.findByIdIn(ids, pageable)
                .stream()
                .map(documentMapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<DocumentDto> searchDocument(DocumentSearchRequest request) {

        Pageable pageable = PageRequest.of(
                request.getSortingRequest().getPage(),
                request.getSortingRequest().getSize(),
                Sort.by(Sort.Direction.fromString(
                                request.getSortingRequest().getDirection().getFieldName()),
                        request.getSortingRequest().getSortBy().getFieldName())
        );

        return documentRepository.filterDocument(request, pageable)
                .stream()
                .map(documentMapper::toDto)
                .toList();
    }


    /**
     * Create draft document and save it
     *
     * @param authorUUID   Author id
     * @param documentName Name of document
     * @return saved document
     */
    public DocumentDto createDraft(UUID authorUUID, String documentName) {
        Objects.requireNonNull(documentName);
        Objects.requireNonNull(authorUUID);

        Author author = authorRepository.getReferenceById(authorUUID);

        Document save = documentRepository.save(new Document()
                .setDocumentNumber(DocumentNumberGenerator.generate())
                .setAuthor(author)
                .setName(documentName)
                .setStatus(DocumentStatus.DRAFT)
                .setCreatedAt(ZonedDateTime.now())
                .setUpdatedAt(ZonedDateTime.now()));

        return documentMapper.toDto(save);
    }


    /**
     * Set All document to status {@link   DocumentStatus#SUBMITTED}
     *
     * @param authorUUID author id
     * @param ids        Document Id
     */

    public List<DocumentProcessedStatusDto> submitDocuments(UUID authorUUID, List<Long> ids) {

        Objects.requireNonNull(authorUUID);
        if (ids == null || ids.isEmpty()) {
            return Collections.emptyList();
        }

        Author author = authorRepository.getReferenceById(authorUUID);

        return ids.stream()
                .map(id -> documentTransactionalService.submitSingle(id, author))
                .toList();

    }

    /**
     * Set All document to status {@link   DocumentStatus#APPROVED}
     *
     * @param authorUUID author id
     * @param ids        Document Id
     */
    public List<DocumentProcessedStatusDto> approveDocuments(UUID authorUUID, List<Long> ids) {
        Objects.requireNonNull(authorUUID);
        if (ids == null || ids.isEmpty()) {
            return Collections.emptyList();
        }

        Author author = authorRepository.getReferenceById(authorUUID);

        return ids.stream()
                .map(id -> {
                    try {
                        return documentTransactionalService.approveSingle(id, author);
                    } catch (DocumentRegisterException e) {
                        return DocumentProcessedStatusDto.registerException(id);
                    }
                })
                .toList();
    }


    public Map<DocumentResultStatus, Integer> concurrentApprove(Integer threads, Integer attempts, Long documentId, UUID authorUuid) {

        Map<DocumentResultStatus, Integer> resultStatus = new ConcurrentHashMap<>();

        List<CompletableFuture<Void>> futures = new ArrayList<>();

        Author author = authorRepository.getReferenceById(authorUuid);

        try (ExecutorService executorService = Executors.newFixedThreadPool(threads)) {

            for (int i = 0; i < attempts; i++) {
                futures.add(CompletableFuture.supplyAsync(() -> approveOneDocuments(author.getId(), documentId), executorService)
                        .thenAccept((result) -> resultStatus.merge(result.status(), 1, Integer::sum)));
            }
        }


        try {
            CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new)).get();
        } catch (InterruptedException | ExecutionException e) {
            throw new DocumentProcessingException(e.getMessage());
        }

        return resultStatus;

    }

    private DocumentProcessedStatusDto approveOneDocuments(UUID authorUUID, Long id) {
        Objects.requireNonNull(authorUUID);

        Author author = authorRepository.getReferenceById(authorUUID);

        try {
            return documentTransactionalService.approveSingle(id, author);
        } catch (DocumentRegisterException e) {
            return DocumentProcessedStatusDto.registerException(id);
        }
    }
}
