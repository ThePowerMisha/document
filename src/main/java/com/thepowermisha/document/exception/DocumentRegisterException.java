package com.thepowermisha.document.exception;

import lombok.Getter;

@Getter
public class DocumentRegisterException extends RuntimeException {
    private final Long documentId;

    public DocumentRegisterException(Long documentId, Throwable cause) {
        super("Failed to create registry record for document " + documentId, cause);
        this.documentId = documentId;
    }

}
