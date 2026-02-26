package com.thepowermisha.scheduler.service;

import com.thepowermisha.document.dto.DocumentDto;
import com.thepowermisha.document.dto.DocumentProcessedStatusDto;
import com.thepowermisha.document.request.DocumentSearchRequest;
import com.thepowermisha.document.service.DocumentService;
import com.thepowermisha.document.type.DocumentStatus;
import com.thepowermisha.scheduler.scheduler.AuthorUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class DocumentSchedulerService {

    private final DocumentService documentService;
    private final AuthorUtil authorUtil;

    @Value("${app.batch-size:100}")
    private Integer batchSize;

    @Transactional
    public void submitProcess() {
        DocumentSearchRequest request = new DocumentSearchRequest().setStatus(DocumentStatus.DRAFT);
        Pageable pageable = PageRequest.of(0, batchSize);

        List<Long> documentIds = documentService.searchDocument(request, pageable)
                .stream()
                .map(DocumentDto::getId)
                .toList();

        log.info("(SUBMIT) Draft documents found: {}", documentIds.size());
        List<DocumentProcessedStatusDto> processedStatusDtos = documentService.submitDocuments(
                documentIds,
                authorUtil.getUserContext());
        log.info("(SUBMIT) Submitted documents count: {}", processedStatusDtos.size());
    }

    @Transactional
    public void approveProcess() {
        DocumentSearchRequest request = new DocumentSearchRequest().setStatus(DocumentStatus.SUBMITTED);

        Pageable pageable = PageRequest.of(0, batchSize);

        List<Long> documentIds = documentService.searchDocument(request, pageable)
                .stream()
                .map(DocumentDto::getId)
                .toList();

        log.info("(APPROVE) Submit documents found: {}", documentIds.size());
        List<DocumentProcessedStatusDto> processedStatusDtos = documentService.approveDocuments(
                documentIds,
                authorUtil.getUserContext());
        log.info("(APPROVE) Approved documents count: {}", processedStatusDtos.size());
    }

}
