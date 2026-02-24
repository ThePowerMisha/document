package com.thepowermisha.document.dto;

import com.thepowermisha.document.entity.Author;
import com.thepowermisha.document.type.DocumentStatus;
import lombok.Data;

import java.time.ZonedDateTime;
import java.util.List;

@Data
public class DocumentDto {
    private Long id;
    private String documentNumber;
    private AuthorDto author;
    private String name;
    private DocumentStatus status;
    private List<DocumentHistoryDto> documentHistory;
    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;
}
