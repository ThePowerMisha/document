package com.thepowermisha.document.dto;

import com.thepowermisha.document.entity.Author;
import com.thepowermisha.document.type.DocumentStatus;
import lombok.Data;

import java.time.ZonedDateTime;

@Data
public class DocumentDto {
    private Long id;
    private String documentNumber;
    private Author author;
    private String name;
    private DocumentStatus status;
    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;
}
