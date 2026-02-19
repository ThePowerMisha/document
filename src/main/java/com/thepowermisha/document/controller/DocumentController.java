package com.thepowermisha.document.controller;

import com.thepowermisha.document.dto.DocumentDto;
import com.thepowermisha.document.dto.DocumentProcessedStatusDto;
import com.thepowermisha.document.dto.ResponseDto;
import com.thepowermisha.document.service.DocumentBatchService;
import com.thepowermisha.document.service.DocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/document")
@RequiredArgsConstructor
public class DocumentController extends AbstractController{

    private final DocumentService documentService;
    private final DocumentBatchService documentBatchService;

    @PutMapping("/create")
    public ResponseDto<DocumentDto> createDraft(@RequestParam String name){
        return ResponseDto.success(documentService.createDraft(getUserContext().getUserId(),name));
    }

    @PostMapping("/submit")
    public ResponseDto<List<DocumentProcessedStatusDto>> submitDocuments(@RequestBody List<Long> ids){
        return ResponseDto.success(documentService.submitDocuments(getUserContext().getUserId(),ids));
    }

    @PostMapping("/submit-batch")
    public ResponseDto<List<DocumentProcessedStatusDto>> submitBatchDocuments(@RequestBody List<Long> ids){
        return ResponseDto.success(documentBatchService.submitBatchDocument(getUserContext().getUserId(),ids));
    }

    @PostMapping("/approve")
    public ResponseDto<List<DocumentProcessedStatusDto>> approveDocuments(@RequestBody List<Long> ids){
        return ResponseDto.success(documentService.approveDocuments(getUserContext().getUserId(),ids));
    }

    @PostMapping("/approve-batch")
    public ResponseDto<List<DocumentProcessedStatusDto>> approveBatchDocuments(@RequestBody List<Long> ids){
        return ResponseDto.success(documentBatchService.approveBatchDocument(getUserContext().getUserId(),ids));
    }



    @GetMapping("/getAll")
    public ResponseDto<List<DocumentDto>> getDocumentsById(@RequestBody List<Long> ids){
        return ResponseDto.success(documentService.getDocumentsList(ids));
    }


    @GetMapping("/{id}")
    public DocumentDto getDocument(@PathVariable Long id){
        return documentService.getDocument(id);
    }


}
