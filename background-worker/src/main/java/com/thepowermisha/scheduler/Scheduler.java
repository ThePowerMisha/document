package com.thepowermisha.scheduler;

import com.thepowermisha.document.dto.DocumentDto;
import com.thepowermisha.document.dto.DocumentProcessedStatusDto;
import com.thepowermisha.document.entity.Author;
import com.thepowermisha.document.request.DocumentSearchRequest;
import com.thepowermisha.document.request.DocumentSortingRequest;
import com.thepowermisha.document.service.DocumentService;
import com.thepowermisha.document.type.DocumentStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@EnableScheduling
@RequiredArgsConstructor
@Slf4j
public class Scheduler {

    private final DocumentService documentService;
    private final AuthorUtil authorUtil;

    @Scheduled(fixedDelayString = "${app.worker.submit-delay}")
    public void submitProcess(){
        DocumentSearchRequest request = new DocumentSearchRequest();

        request.setStatus(DocumentStatus.DRAFT);
        request.setSortingRequest(new DocumentSortingRequest());

        List<Long> documentIds = documentService.searchDocument(request)
                .stream()
                .map(DocumentDto::getId)
                .toList();
        Author author = authorUtil.getAuthor();

        log.info("(SUBMIT) Author name: {}",author.getName());
        log.info("(SUBMIT) Draft documents count: {}", documentIds.size());
        List<DocumentProcessedStatusDto> processedStatusDtos = documentService.submitDocuments(author.getId(), documentIds);
        log.info("(SUBMIT) Submitted documents count: {}",processedStatusDtos.size());
    }

    @Scheduled(fixedDelayString = "${app.worker.approve-delay}")
    public void approveProcess(){
        DocumentSearchRequest request = new DocumentSearchRequest();

        request.setStatus(DocumentStatus.SUBMITTED);
        request.setSortingRequest(new DocumentSortingRequest());

        List<Long> documentIds = documentService.searchDocument(request)
                .stream()
                .map(DocumentDto::getId)
                .toList();
        Author author = authorUtil.getAuthor();

        log.info("(APPROVE) Author name: {}",author.getName());
        log.info("(APPROVE) Submitted documents count: {}", documentIds.size());
        List<DocumentProcessedStatusDto> processedStatusDtos = documentService.approveDocuments(author.getId(), documentIds);
        log.info("(APPROVE) Approved documents count: {}",processedStatusDtos.size());
    }

}
