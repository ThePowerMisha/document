package com.thepowermisha.document.dto;

import com.thepowermisha.document.type.DocumentHistoryAction;
import lombok.Data;

import java.time.ZonedDateTime;
import java.util.UUID;

@Data
public class DocumentHistoryDto {
    private UUID id;
    private AuthorDto author;
    private DocumentHistoryAction action;
    private ZonedDateTime createdAt;
    private String commentary;
}
