package com.thepowermisha.document.service;

import com.google.common.collect.Lists;
import com.thepowermisha.document.dto.DocumentProcessedStatusDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class DocumentBatchService {

    private final DocumentService documentService;

    @Value("${document.batch:1000}")
    private Integer batchSize;

    public List<DocumentProcessedStatusDto> submitBatchDocument(UUID authorUUID, List<Long> ids) {
        return Lists.partition(ids, batchSize).stream()
                .map(i -> documentService.submitDocuments(authorUUID, i))
                .flatMap(Collection::stream)
                .toList();
    }

    public List<DocumentProcessedStatusDto> approveBatchDocument(UUID authorUUID, List<Long> ids) {
        return Lists.partition(ids, batchSize).stream()
                .map(i -> documentService.approveDocuments(authorUUID, i))
                .flatMap(Collection::stream)
                .toList();
    }
}
