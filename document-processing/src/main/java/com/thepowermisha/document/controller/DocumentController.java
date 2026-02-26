package com.thepowermisha.document.controller;

import com.thepowermisha.document.dto.*;
import com.thepowermisha.document.request.*;
import com.thepowermisha.document.security.UserContextHolder;
import com.thepowermisha.document.service.DocumentBatchService;
import com.thepowermisha.document.service.DocumentService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/document")
@RequiredArgsConstructor
public class DocumentController{

    private final DocumentService documentService;
    private final DocumentBatchService documentBatchService;


    @GetMapping("/get-documents")
    public ResponseDto<List<DocumentDto>> getDocumentsById(
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseDto.success(documentService.getDocumentsList(pageable));
    }

    @GetMapping("/get-ids-sorted")
    public ResponseDto<List<DocumentDto>> getDocumentsById(
            @Valid @RequestBody List<@NotNull Long> documentIds,
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return ResponseDto.success(documentService.getDocumentsListByIds(documentIds, pageable));
    }

    @GetMapping("/{id}")
    public ResponseDto<DocumentWithHistoryDto> getDocument(@PathVariable Long id) {
        return ResponseDto.success(documentService.getDocument(id));
    }

    @PostMapping("/search")
    public ResponseDto<List<DocumentDto>> search(
            @Valid @RequestBody DocumentSearchRequest request,
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseDto.success(documentService.searchDocument(request, pageable));
    }

    @PostMapping("/concurrent")
    public ResponseDto<ConcurrentApproveDocumentDto> concurrentApprove(@Valid @RequestBody ConcurrentTestRequest request) {
        return ResponseDto.success(documentService.concurrentApprove(request));
    }


    @PutMapping("/create")
    public ResponseDto<DocumentDto> createDraft(@Valid @RequestBody DocumentCreateRequest name) {
        if(UserContextHolder.getCurrentUser() == null){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        return ResponseDto.success(documentService.createDraft(name));
    }

    @PostMapping("/submit")
    public ResponseDto<List<DocumentProcessedStatusDto>> submitDocuments(@Valid @RequestBody List<@NotNull Long> ids) {
        if (ids.size() > 1000) {
            throw new IllegalArgumentException("Maximum 1000 documents per request");
        }
        if(UserContextHolder.getCurrentUser() == null){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        return ResponseDto.success(documentService.submitDocuments(ids));
    }

    @PostMapping("/submit-batch")
    public ResponseDto<List<DocumentProcessedStatusDto>> submitBatchDocuments(@Valid @RequestBody List<@NotNull Long> ids) {
        if(UserContextHolder.getCurrentUser() == null){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        return ResponseDto.success(documentBatchService.submitBatchDocument(ids));
    }

    @PostMapping("/approve")
    public ResponseDto<List<DocumentProcessedStatusDto>> approveDocuments(@Valid @RequestBody List<@NotNull Long> ids) {
        if(UserContextHolder.getCurrentUser() == null){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        if (ids.size() > 1000) {
            throw new IllegalArgumentException("Maximum 1000 documents per request");
        }
        return ResponseDto.success(documentService.approveDocuments(ids));
    }

    @PostMapping("/approve-batch")
    public ResponseDto<List<DocumentProcessedStatusDto>> approveBatchDocuments(@Valid @RequestBody List<@NotNull Long> ids) {
        if(UserContextHolder.getCurrentUser() == null){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        return ResponseDto.success(documentBatchService.approveBatchDocument(ids));
    }

}
