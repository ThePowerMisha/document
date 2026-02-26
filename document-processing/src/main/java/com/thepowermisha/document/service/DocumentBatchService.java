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

    public List<DocumentProcessedStatusDto> submitBatchDocument(List<Long> ids) {
        return Lists.partition(ids, batchSize).stream()
                .map(documentService::submitDocuments)
                .flatMap(Collection::stream)
                .toList();
    }

    public List<DocumentProcessedStatusDto> approveBatchDocument(List<Long> ids) {
        return Lists.partition(ids, batchSize).stream()
                .map(documentService::approveDocuments)
                .flatMap(Collection::stream)
                .toList();
    }
}
