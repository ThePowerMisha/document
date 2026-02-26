package com.thepowermisha.document.exception;

import lombok.Getter;

@Getter
public class DocumentNotFoundException extends RuntimeException {
    private final Long documentId;

    public DocumentNotFoundException(Long documentId) {
        super("Can't find document with id:  " + documentId);
        this.documentId = documentId;
    }
}
