package com.thepowermisha.document.service;

import com.thepowermisha.document.dto.ConcurrentApproveDocumentDto;
import com.thepowermisha.document.dto.DocumentDto;
import com.thepowermisha.document.dto.DocumentProcessedStatusDto;
import com.thepowermisha.document.dto.DocumentWithHistoryDto;
import com.thepowermisha.document.entity.Author;
import com.thepowermisha.document.entity.Document;
import com.thepowermisha.document.exception.AuthorNotFoundException;
import com.thepowermisha.document.exception.DocumentNotFoundException;
import com.thepowermisha.document.exception.DocumentProcessingException;
import com.thepowermisha.document.exception.DocumentRegisterException;
import com.thepowermisha.document.mapper.DocumentMapper;
import com.thepowermisha.document.repository.AuthorRepository;
import com.thepowermisha.document.repository.DocumentRepository;
import com.thepowermisha.document.request.ConcurrentTestRequest;
import com.thepowermisha.document.request.DocumentCreateRequest;
import com.thepowermisha.document.request.DocumentSearchRequest;
import com.thepowermisha.document.security.UserContext;
import com.thepowermisha.document.security.UserContextHolder;
import com.thepowermisha.document.type.DocumentResultStatus;
import com.thepowermisha.document.type.DocumentStatus;
import com.thepowermisha.document.util.DocumentNumberGenerator;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.*;
import java.util.concurrent.*;


@Service
@RequiredArgsConstructor
@Slf4j
public class DocumentService {
    private final DocumentRepository documentRepository;

    private final DocumentTransactionalService documentTransactionalService;

    private final AuthorRepository authorRepository;

    private final DocumentMapper documentMapper;

    /**
     * @param id Document id
     * @return return Document by id
     */
    public DocumentWithHistoryDto getDocument(Long id) {
        return documentRepository.findWithHistoryById(id)
                .map(documentMapper::toHistoryDto)
                .orElseThrow(() -> new DocumentNotFoundException(id));
    }

    /**
     * @param pageable Sorting request
     * @return Sorted Documents
     */
    public List<DocumentDto> getDocumentsList(Pageable pageable) {
        return documentRepository.findAll(pageable)
                .stream()
                .map(documentMapper::toDto)
                .toList();
    }

    /**
     * @param ids      Document ids
     * @param pageable Sorting request
     * @return Sorted Documents in ids
     */
    public List<DocumentDto> getDocumentsListByIds(List<Long> ids, Pageable pageable) {
        return documentRepository.findByIdsWithPagination(ids, pageable)
                .stream()
                .map(documentMapper::toDto)
                .toList();
    }

    /**
     * @param request  Filer for documents
     * @param pageable Sorting request
     * @return Filtered documents
     */
    @Transactional(readOnly = true)
    public List<DocumentDto> searchDocument(DocumentSearchRequest request, Pageable pageable) {
        return documentRepository.filterDocument(request, pageable)
                .stream()
                .map(documentMapper::toDto)
                .toList();
    }

    /**
     * Create draft document and save it
     *
     * @param request Request with name of document
     * @return saved document
     */
    @Transactional
    public DocumentDto createDraft(DocumentCreateRequest request) {
        return createDraft(request, null);
    }

    /**
     * Create draft document and save it
     *
     * @param documentName Name of document
     * @param userContext  UserContext
     * @return saved document
     */
    @Transactional
    public DocumentDto createDraft(DocumentCreateRequest documentName, @Nullable UserContext userContext) {
        var user = Optional.ofNullable(userContext)
                .orElse(UserContextHolder.getCurrentUser());

        if (!authorRepository.existsById(user.userId())) {
            throw new AuthorNotFoundException("Author not found. Author id: " + user.userId());
        }

        return documentMapper.toDto(
                documentRepository.save(new Document()
                        .setDocumentNumber(DocumentNumberGenerator.generate())
                        .setAuthor(authorRepository.getReferenceById(user.userId()))
                        .setName(documentName.getTitle())
                        .setStatus(DocumentStatus.DRAFT)
                        .setCreatedAt(ZonedDateTime.now())
                        .setUpdatedAt(ZonedDateTime.now())));
    }


    /**
     * Set All document to status {@link   DocumentStatus#SUBMITTED}
     *
     * @param ids Document Id
     * @return List of document submitted status
     */
    public List<DocumentProcessedStatusDto> submitDocuments(List<Long> ids) {
        return submitDocuments(ids, UserContextHolder.getCurrentUser());
    }

    /**
     * Set All document to status {@link   DocumentStatus#SUBMITTED}
     *
     * @param ids     Document Id
     * @param context Author of changes
     * @return List of document submitted status
     */
    public List<DocumentProcessedStatusDto> submitDocuments(List<Long> ids, UserContext context) {
        UUID authorUUID = context.userId();
        if (!authorRepository.existsById(authorUUID)) {
            throw new AuthorNotFoundException("Author not found. Author id: " + authorUUID);
        }

        Author author = authorRepository.getReferenceById(authorUUID);

        return ids.stream()
                .map(id -> {
                    try {
                        return documentTransactionalService.submitSingle(id, author);
                    } catch (DataAccessException e) {
                        log.error("Can't submit document id {}", id);
                        return DocumentProcessedStatusDto.conflict(id);
                    }
                })
                .toList();
    }

    /**
     * Set All document to status {@link   DocumentStatus#SUBMITTED}
     *
     * @param ids Document Id
     * @return List of document submitted status
     */
    public List<DocumentProcessedStatusDto> approveDocuments(List<Long> ids) {
        return approveDocuments(ids, UserContextHolder.getCurrentUser());
    }

    /**
     * Set All document to status {@link   DocumentStatus#APPROVED}
     *
     * @param ids     Document Id
     * @param context Author of changes
     * @return List of document approved status
     */
    public List<DocumentProcessedStatusDto> approveDocuments(List<Long> ids, UserContext context) {
        UUID authorUUID = context.userId();

        if (!authorRepository.existsById(authorUUID)) {
            throw new AuthorNotFoundException("Author not found. Author id: " + authorUUID);
        }

        Author author = authorRepository.getReferenceById(authorUUID);

        return ids.stream()
                .map(id -> {
                    try {
                        return documentTransactionalService.approveSingle(id, author);
                    } catch (DocumentRegisterException e) {
                        log.error("Can't approve document id {}", id);
                        return DocumentProcessedStatusDto.registerException(id);
                    }
                })
                .toList();
    }

    /**
     * @param request requets with id, threads and attempts
     * @return Status of document and map of status to approve
     */
    public ConcurrentApproveDocumentDto concurrentApprove(ConcurrentTestRequest request) {
        UUID authorUUID = UserContextHolder.getCurrentUser().userId();

        if (!authorRepository.existsById(authorUUID)) {
            throw new AuthorNotFoundException("Author not found. Author id: " + authorUUID);
        }

        if (!documentRepository.existsById(request.getDocumentId())) {
            throw new DocumentNotFoundException(request.getDocumentId());
        }

        Map<DocumentResultStatus, Integer> resultStatus = new ConcurrentHashMap<>();

        List<CompletableFuture<Void>> futures = new ArrayList<>();

        Author author = authorRepository.getReferenceById(authorUUID);

        try (ExecutorService executorService = Executors.newFixedThreadPool(request.getThreads())) {

            for (int i = 0; i < request.getAttempts(); i++) {
                futures.add(CompletableFuture.supplyAsync(() -> approveOneDocuments(author.getId(), request.getDocumentId()), executorService)
                        .thenAccept((result) -> resultStatus.merge(result.status(), 1, Integer::sum)));
            }
        }

        try {
            CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new)).get();
        } catch (InterruptedException | ExecutionException e) {
            Thread.currentThread().interrupt();
            throw new DocumentProcessingException(e.getMessage());
        }

        return ConcurrentApproveDocumentDto.builder()
                .documentId(request.getDocumentId())
                .totalAttempts(request.getAttempts())
                .successfulAttempts(resultStatus.getOrDefault(DocumentResultStatus.SUCCESS, 0))
                .conflictAttempts(resultStatus.getOrDefault(DocumentResultStatus.CONFLICT, 0))
                .notFoundAttempts(resultStatus.getOrDefault(DocumentResultStatus.NOT_FOUND, 0))
                .registryErrorAttempts(resultStatus.getOrDefault(DocumentResultStatus.REGISTRATION_ERROR, 0))
                .build();
    }

    /**
     * @param authorUUID Author
     * @param id         Document Id
     * @return status of approve single document
     */
    private DocumentProcessedStatusDto approveOneDocuments(UUID authorUUID, Long id) {
        Objects.requireNonNull(authorUUID);

        Author author = authorRepository.getReferenceById(authorUUID);

        try {
            return documentTransactionalService.approveSingle(id, author);
        } catch (DocumentRegisterException e) {
            log.error("Can't approve document id {}", id);
            return DocumentProcessedStatusDto.registerException(id);
        }
    }
}
