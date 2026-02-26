package com.thepowermisha.document.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class DocumentCreateRequest {
    @NotBlank
    private String title;
}
