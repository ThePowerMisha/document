package com.thepowermisha.document.request;

import com.thepowermisha.document.type.DocumentStatus;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.ZonedDateTime;
import java.util.UUID;

@Data
@Accessors(chain = true)
public class DocumentSearchRequest {
    private DocumentStatus status;
    private UUID authorId;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private ZonedDateTime dateFrom;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private ZonedDateTime dateTo;
}
