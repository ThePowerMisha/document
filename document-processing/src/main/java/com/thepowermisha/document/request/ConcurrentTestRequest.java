package com.thepowermisha.document.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


@Data
public class ConcurrentTestRequest {
    @NotNull
    private Long documentId;

    @Min(1)
    private int threads = 5;

    @Min(1)
    private int attempts = 10;
}
