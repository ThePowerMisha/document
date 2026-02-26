package com.thepowermisha.document.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class DocumentCreateRequest {
    @NotBlank
    private String title;
}
