package com.thepowermisha.document.service;

import com.google.common.collect.Lists;
import com.thepowermisha.document.dto.DocumentProcessedStatusDto;
import com.thepowermisha.document.entity.Document;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class DocumentBatchService {

    private DocumentService documentService;

    @Value("${document.batch:1000}")
    private Integer batchSize;

    public List<DocumentProcessedStatusDto> submitBatchDocument(UUID authorUUID, List<Long> ids) {
        return Lists.partition(ids, batchSize).stream()
                .map(i -> documentService.submitDocuments(authorUUID, i))
                .flatMap(Collection::stream)
                .toList();
    }



//    private Map<Long, Document> loadDocumentsPartitioned(List<Long> ids) {
//
//        Map<Long, Document> result = new HashMap<>(ids.size());
//
//        for (List<Long> batch : Lists.partition(ids, BATCH_SIZE)) {
//            List<Document> documents = documentRepository.findAllById(batch);
//            for (Document document : documents) {
//                result.put(document.getId(), document);
//            }
//        }
//
//        return result;
//    }
}
