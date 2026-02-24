package com.thepowermisha.document.request;

import com.thepowermisha.document.type.DocumentStatus;
import lombok.Data;

import java.time.ZonedDateTime;
import java.util.UUID;

@Data
public class DocumentSearchRequest {
    private DocumentStatus status;
    private UUID authorId;
    private ZonedDateTime dateFrom;
    private ZonedDateTime dateTo;
    private DocumentSortingRequest sortingRequest;
}
