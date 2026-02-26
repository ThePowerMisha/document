package com.thepowermisha.document.controller;

import com.thepowermisha.document.dto.ResponseDto;
import com.thepowermisha.document.exception.AuthorNotFoundException;
import com.thepowermisha.document.exception.DocumentNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;


@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ResponseDto<Void>> handleIllegalArgument(IllegalArgumentException ex) {
        return ResponseEntity
                .badRequest()
                .body(ResponseDto.error("IllegalArgumentException",ex.getMessage()));
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ResponseDto<Void>> handleResponseStatus(ResponseStatusException ex){
        return ResponseEntity
                .badRequest()
                .body(ResponseDto.error("Unauthorized",ex.getMessage()));
    }

    @ExceptionHandler(AuthorNotFoundException.class)
    public ResponseEntity<ResponseDto<Void>> handleAuthorNotFound(AuthorNotFoundException ex){
        return ResponseEntity
                .badRequest()
                .body(ResponseDto.error("Author not found",ex.getMessage()));
    }

    @ExceptionHandler(DocumentNotFoundException.class)
    public ResponseEntity<ResponseDto<Void>> handleDocumentNotFound(DocumentNotFoundException ex){
        return ResponseEntity
                .badRequest()
                .body(ResponseDto.error("Document not Found",ex.getMessage()));
    }
}
