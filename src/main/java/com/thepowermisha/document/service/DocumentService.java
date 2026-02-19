package com.thepowermisha.document.service;

import com.thepowermisha.document.dto.DocumentDto;
import com.thepowermisha.document.dto.DocumentProcessedStatusDto;
import com.thepowermisha.document.entity.Author;
import com.thepowermisha.document.entity.Document;
import com.thepowermisha.document.exception.DocumentRegisterException;
import com.thepowermisha.document.mapper.DocumentMapper;
import com.thepowermisha.document.repository.AuthorRepository;
import com.thepowermisha.document.repository.DocumentRepository;
import com.thepowermisha.document.type.DocumentStatus;
import com.thepowermisha.document.util.DocumentNumberGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.*;


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
        return documentMapper.toDto(documentRepository.findById(id).orElseThrow());
    }

    /**
     * @param id List Document id
     * @return return list of Document
     */
    public List<DocumentDto> getDocumentsList(List<Long> id) {
        return documentRepository.findAllById(id)
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
    @Transactional
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

}
