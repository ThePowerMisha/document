package com.thepowermisha.document.dto;

import com.thepowermisha.document.type.DocumentResultStatus;

public record DocumentProcessedStatusDto(Long id, DocumentResultStatus status) {
    public static DocumentProcessedStatusDto notFound(Long id) {
        return new DocumentProcessedStatusDto(id, DocumentResultStatus.NOT_FOUND);
    }

    public static DocumentProcessedStatusDto conflict(Long id) {
        return new DocumentProcessedStatusDto(id, DocumentResultStatus.CONFLICT);

    }

    public static DocumentProcessedStatusDto success(Long id) {
        return new DocumentProcessedStatusDto(id, DocumentResultStatus.SUCCESS);
    }

    public static DocumentProcessedStatusDto registerException(Long id) {
        return new DocumentProcessedStatusDto(id, DocumentResultStatus.REGISTRATION_ERROR);
    }
}
