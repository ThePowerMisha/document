package com.thepowermisha.document.dto;


import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class ConcurrentApproveDocumentDto {
    private Long documentId;
    private int totalAttempts;
    private int successfulAttempts;
    private int conflictAttempts;
    private int registryErrorAttempts;
    private int notFoundAttempts;
}
