package com.thepowermisha.document.service;

import com.google.common.collect.Lists;
import com.thepowermisha.document.dto.DocumentDto;
import com.thepowermisha.document.dto.DocumentProcessedStatusDto;
import com.thepowermisha.document.entity.Author;
import com.thepowermisha.document.entity.Document;
import com.thepowermisha.document.entity.DocumentHistory;
import com.thepowermisha.document.entity.DocumentRegister;
import com.thepowermisha.document.mapper.DocumentMapper;
import com.thepowermisha.document.repository.AuthorRepository;
import com.thepowermisha.document.repository.DocumentHistoryRepository;
import com.thepowermisha.document.repository.DocumentRegisterRepository;
import com.thepowermisha.document.repository.DocumentRepository;
import com.thepowermisha.document.type.DocumentHistoryAction;
import com.thepowermisha.document.type.DocumentResultStatus;
import com.thepowermisha.document.type.DocumentStatus;
import com.thepowermisha.document.util.DocumentNumberGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 *
 */
@Service
@RequiredArgsConstructor
public class DocumentService {
    private final DocumentRepository documentRepository;
    private final DocumentHistoryRepository documentHistoryRepository;
    private final DocumentRegisterRepository documentRegisterRepository;

    private final AuthorRepository authorRepository;

    private final DocumentMapper documentMapper;

    public DocumentDto getDocument(Long id) {
        return null;
    }

    public List<DocumentDto> getDocumentsList(List<Long> id) {
        return null;
    }


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
     * @param authorUUID
     * @param ids        Document Id
     */
    @Transactional
    public List<DocumentProcessedStatusDto> submitDocuments(UUID authorUUID, List<Long> ids) {

        Objects.requireNonNull(authorUUID);
        if (ids == null || ids.isEmpty()) {
            return Collections.emptyList();
        }

        Author author = authorRepository.getReferenceById(authorUUID);
        Map<Long, Document> dbDocuments = documentRepository.getAll(ids)
                .stream()
                .collect(Collectors.toMap(Document::getId, Function.identity()));

        return ids.stream()
                .map(id -> submitSingle(dbDocuments.get(id), id, author))
                .toList();

    }

    private DocumentProcessedStatusDto submitSingle(Document document, Long id, Author author) {

        Optional<Document> optional = Optional.ofNullable(document);
        if (optional.isEmpty()) {
            return DocumentProcessedStatusDto.notFound(id);
        }

        Document doc = optional.get();

        if (doc.getStatus() != DocumentStatus.DRAFT) {
            return DocumentProcessedStatusDto.conflict(id);
        }

        doc.setStatus(DocumentStatus.SUBMITTED);
        doc.setUpdatedAt(ZonedDateTime.now());

        DocumentProcessedStatusDto documentProcessed = submitDocumentStatus(doc);

        if (documentProcessed.status() == DocumentResultStatus.SUCCESS) {
            documentHistoryRepository.save(
                    submit(doc, author, ZonedDateTime.now())
            );
        }

        return documentProcessed;
    }

    private DocumentHistory submit(Document document, Author author, ZonedDateTime date) {
        DocumentHistory history = new DocumentHistory();
        history.setDocument(document);
        history.setAuthor(author);
        history.setAction(DocumentHistoryAction.SUBMIT);
        history.setCreatedAt(date);
        return history;
    }

    private DocumentProcessedStatusDto submitDocumentStatus(Document document) {
        try {
            documentRepository.updateStatus(document.getId(), DocumentStatus.SUBMITTED);
            return new DocumentProcessedStatusDto(document.getId(), DocumentResultStatus.SUCCESS);
        } catch (DataAccessException e) {
            return new DocumentProcessedStatusDto(document.getId(), DocumentResultStatus.CONFLICT);
        }
    }
}
