package com.thepowermisha.document.dto;

import com.thepowermisha.document.type.DocumentStatus;
import lombok.Data;

import java.time.ZonedDateTime;
import java.util.UUID;

@Data
public class DocumentDto {
    private Long id;
    private UUID uid;
    private String author;
    private String name;
    private DocumentStatus status;
    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;
}
