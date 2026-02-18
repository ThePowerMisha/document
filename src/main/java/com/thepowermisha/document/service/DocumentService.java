package com.thepowermisha.document.service;

import com.google.common.collect.Lists;
import com.thepowermisha.document.dto.DocumentDto;
import com.thepowermisha.document.entity.Author;
import com.thepowermisha.document.entity.Document;
import com.thepowermisha.document.mapper.DocumentMapper;
import com.thepowermisha.document.repository.AuthorRepository;
import com.thepowermisha.document.repository.DocumentHistoryRepository;
import com.thepowermisha.document.repository.DocumentRepository;
import com.thepowermisha.document.type.DocumentResultStatus;
import com.thepowermisha.document.type.DocumentStatus;
import com.thepowermisha.document.util.DocumentNumberGenerator;
import lombok.RequiredArgsConstructor;
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

    private final AuthorRepository authorRepository;

    private final DocumentMapper documentMapper;

    public DocumentDto getDocument(Long id){
        return null;
    }

    public List<DocumentDto> getDocumentsList(List<Long> id){
        return null;
    }


    public DocumentDto createDraft(UUID uuid, String name) {
        Objects.requireNonNull(name);
        Objects.requireNonNull(uuid);

        Author author = authorRepository.getReferenceById(uuid);

        Document save = documentRepository.save(new Document()
                .setDocumentNumber(DocumentNumberGenerator.generate())
                .setAuthor(author)
                .setName(name)
                .setStatus(DocumentStatus.DRAFT)
                .setCreatedAt(ZonedDateTime.now())
                .setUpdatedAt(ZonedDateTime.now()));

        return documentMapper.toDto(save);
    }

    /**
     * Set All document to status {@link   DocumentStatus#SUBMITTED}
     * @param ids Document Id
     */
    @Transactional
    public Map<Long, DocumentResultStatus> submitDocuments(List<Long> ids){
        List<List<Long>> partition = Lists.partition(ids, 1000);

        List<Document> list = partition.stream()
                .map(documentRepository::getAll)
                .flatMap(Collection::stream).toList();

        //

        Set<Long> idsInDb = list.stream()
                .map(Document::getId)
                .collect(Collectors.toSet());

        List<Document> notDraft = list.stream()
                .filter(i -> i.getStatus() != DocumentStatus.DRAFT)
                .toList();

        List<Document> draft = list.stream()
                .filter(i -> i.getStatus() == DocumentStatus.DRAFT)
                .peek(i-> {
                    i.setStatus(DocumentStatus.SUBMITTED);
                    i.setUpdatedAt(ZonedDateTime.now());
                })
                .toList();

        List<Long> notContained = ids.stream()
                .filter(i -> !idsInDb.contains(i))
                .toList();

        List<Document> updatedDraft = documentRepository.saveAll(draft);

        Map<Long, DocumentResultStatus> notContainedMap = notContained.stream().collect(Collectors.toMap(Function.identity(), status-> DocumentResultStatus.NOT_FOUND));
        Map<Long, DocumentResultStatus> draftMap = updatedDraft.stream().map(Document::getId).collect(Collectors.toMap(Function.identity(), status-> DocumentResultStatus.SUCCESS));
        Map<Long, DocumentResultStatus> notDraftMap = notDraft.stream().map(Document::getId).collect(Collectors.toMap(Function.identity(), status-> DocumentResultStatus.CONFLICT));


        Map<Long, DocumentResultStatus> resultStatusMap = new HashMap<>();

        resultStatusMap.putAll(notContainedMap);
        resultStatusMap.putAll(draftMap);
        resultStatusMap.putAll(notDraftMap);

        return resultStatusMap;

        // TODO MAP TO <DOCUMENTID , ENUM STATUS>
        // ADD METHODS
    }
}
