package com.thepowermisha.document.dto;

import com.thepowermisha.document.entity.Creator;
import com.thepowermisha.document.type.DocumentStatus;
import lombok.Data;

import java.time.ZonedDateTime;
import java.util.UUID;

@Data
public class DocumentDto {
    private Long id;
    private String documentNumber;
    private Creator author;
    private String name;
    private DocumentStatus status;
    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;
}
