package com.thepowermisha.document.service;

import com.thepowermisha.document.dto.DocumentProcessedStatusDto;
import com.thepowermisha.document.entity.Author;
import com.thepowermisha.document.entity.Document;
import com.thepowermisha.document.entity.DocumentHistory;
import com.thepowermisha.document.entity.DocumentRegister;
import com.thepowermisha.document.exception.DocumentRegisterException;
import com.thepowermisha.document.repository.DocumentHistoryRepository;
import com.thepowermisha.document.repository.DocumentRegisterRepository;
import com.thepowermisha.document.repository.DocumentRepository;
import com.thepowermisha.document.type.DocumentHistoryAction;
import com.thepowermisha.document.type.DocumentResultStatus;
import com.thepowermisha.document.type.DocumentStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DocumentTransactionalService {

    private final DocumentRepository documentRepository;
    private final DocumentHistoryRepository documentHistoryRepository;
    private final DocumentRegisterRepository documentRegisterRepository;

    /**
     * Change Document Status from {@link DocumentStatus#DRAFT} to {@link DocumentStatus#SUBMITTED}
     * @param id       Document id
     * @param author   Author who changed document
     * @return return status of document {@link DocumentProcessedStatusDto} where id is document id, and {@link DocumentStatus} is status
     */
    @Transactional
    public DocumentProcessedStatusDto submitSingle( Long id, Author author) {
        Optional<Document> doc = documentRepository.findById(id);

        if (doc.isEmpty()) {
            return DocumentProcessedStatusDto.notFound(id);
        }

        Document document = doc.get();

        if (document.getStatus() != DocumentStatus.DRAFT) {
            return DocumentProcessedStatusDto.conflict(id);
        }

        document.setStatus(DocumentStatus.SUBMITTED);
        document.setUpdatedAt(ZonedDateTime.now());

        DocumentProcessedStatusDto documentProcessed = updateDocumentStatus(document);

        if (documentProcessed.status() == DocumentResultStatus.SUCCESS) {
            documentHistoryRepository.save(
                    createDocumentForHistory(document, author, DocumentHistoryAction.SUBMIT, ZonedDateTime.now())
            );
        }

        return documentProcessed;
    }

    /**
     * Change Document Status from {@link DocumentStatus#SUBMITTED} to {@link DocumentStatus#APPROVED} and write to Register
     * @param id       Document id
     * @param author   Author who changed document
     * @return return status of document {@link DocumentProcessedStatusDto} where id is document id, and {@link DocumentStatus} is status
     */
    @Transactional
    public DocumentProcessedStatusDto approveSingle(Long id, Author author) {
        Optional<Document> doc = documentRepository.findById(id);

        if (doc.isEmpty()) {
            return DocumentProcessedStatusDto.notFound(id);
        }

        Document document = doc.get();

        if (document.getStatus() != DocumentStatus.SUBMITTED) {
            return DocumentProcessedStatusDto.conflict(id);
        }

        document.setStatus(DocumentStatus.APPROVED);
        document.setUpdatedAt(ZonedDateTime.now());

        try {
            documentRegisterRepository.saveAndFlush(
                    createDocumentForRegister(document, ZonedDateTime.now())
            );
        } catch (DataIntegrityViolationException e) {
            throw new DocumentRegisterException(id, e);
        }

        DocumentProcessedStatusDto result = updateDocumentStatus(document);

        if (result.status() == DocumentResultStatus.SUCCESS) {
            documentHistoryRepository.save(
                    createDocumentForHistory(document, author,
                            DocumentHistoryAction.APPROVE,
                            ZonedDateTime.now())
            );
        }

        return result;
    }

    private DocumentProcessedStatusDto updateDocumentStatus(Document document) {
        try {
            documentRepository.updateStatus(document.getId(), document.getStatus());
            return DocumentProcessedStatusDto.success(document.getId());
        } catch (DataAccessException e) {
            return DocumentProcessedStatusDto.conflict(document.getId());
        }
    }

    private DocumentProcessedStatusDto registerDocument(Document document) {
        try {
            documentRegisterRepository.saveAndFlush(
                    createDocumentForRegister(document, ZonedDateTime.now())
            );
            return DocumentProcessedStatusDto.success(document.getId());
        } catch (DataAccessException e) {
            return DocumentProcessedStatusDto.registerException(document.getId());
        }
    }

    /**
     * @param document
     * @param author   Author of changes
     * @param status   Status of document
     * @param date     date
     * @return new {@link DocumentHistory} entity for given document
     */
    private DocumentHistory createDocumentForHistory(Document document, Author author, DocumentHistoryAction status, ZonedDateTime date) {
        DocumentHistory history = new DocumentHistory();
        history.setDocument(document);
        history.setAuthor(author);
        history.setAction(status);
        history.setCreatedAt(date);
        return history;
    }

    private DocumentRegister createDocumentForRegister(Document document, ZonedDateTime date) {
        DocumentRegister register = new DocumentRegister();
        register.setDocument(document);
        register.setDate(date);
        return register;
    }
}
