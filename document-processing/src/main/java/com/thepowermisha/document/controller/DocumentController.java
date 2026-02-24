package com.thepowermisha.document.controller;

import com.thepowermisha.document.dto.DocumentDto;
import com.thepowermisha.document.dto.DocumentProcessedStatusDto;
import com.thepowermisha.document.dto.ResponseDto;
import com.thepowermisha.document.request.DocumentPaginationRequest;
import com.thepowermisha.document.request.DocumentSearchRequest;
import com.thepowermisha.document.request.DocumentSortingRequest;
import com.thepowermisha.document.service.DocumentBatchService;
import com.thepowermisha.document.service.DocumentService;
import com.thepowermisha.document.type.DocumentResultStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/document")
@RequiredArgsConstructor
public class DocumentController extends AbstractController {

    private final DocumentService documentService;
    private final DocumentBatchService documentBatchService;


    @GetMapping("/get-documents")
    public ResponseDto<List<DocumentDto>> getDocumentsById(@RequestBody DocumentSortingRequest request) {
        return ResponseDto.success(documentService.getDocumentsList(request));
    }

    @GetMapping("/get-ids-sorted")
    public ResponseDto<List<DocumentDto>> getDocumentsById(@RequestBody DocumentPaginationRequest document) {
        return ResponseDto.success(documentService.getDocumentsListByIds(document.getIds(),document.getRequest()));
    }


    @GetMapping("/{id}")
    public ResponseDto<DocumentDto> getDocument(@PathVariable Long id) {
        return ResponseDto.success(documentService.getDocument(id));
    }

    @PostMapping("/search")
    public ResponseDto<List<DocumentDto>> search(@RequestBody DocumentSearchRequest request) {
        return ResponseDto.success(documentService.searchDocument(request));
    }

    @PostMapping("/concurrent")
    public ResponseDto<Map<DocumentResultStatus, Integer>> concurrentApprove(@RequestParam Integer threads,
                                                                @RequestParam Integer attempts,
                                                                @RequestParam Long documentId) {
        return ResponseDto.success(documentService.concurrentApprove(
                threads,
                attempts,
                documentId,
                getUserContext().getUserId())
        );
    }


    @PutMapping("/create")
    public ResponseDto<DocumentDto> createDraft(@RequestParam String name) {
        return ResponseDto.success(documentService.createDraft(getUserContext().getUserId(), name));
    }

    @PostMapping("/submit")
    public ResponseDto<List<DocumentProcessedStatusDto>> submitDocuments(@RequestBody List<Long> ids) {
        return ResponseDto.success(documentService.submitDocuments(getUserContext().getUserId(), ids));
    }

    @PostMapping("/submit-batch")
    public ResponseDto<List<DocumentProcessedStatusDto>> submitBatchDocuments(@RequestBody List<Long> ids) {
        return ResponseDto.success(documentBatchService.submitBatchDocument(getUserContext().getUserId(), ids));
    }

    @PostMapping("/approve")
    public ResponseDto<List<DocumentProcessedStatusDto>> approveDocuments(@RequestBody List<Long> ids) {
        return ResponseDto.success(documentService.approveDocuments(getUserContext().getUserId(), ids));
    }

    @PostMapping("/approve-batch")
    public ResponseDto<List<DocumentProcessedStatusDto>> approveBatchDocuments(@RequestBody List<Long> ids) {
        return ResponseDto.success(documentBatchService.approveBatchDocument(getUserContext().getUserId(), ids));
    }

}
