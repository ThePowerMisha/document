package com.thepowermisha.document.request;

import lombok.Data;

import java.util.List;

@Data
public class DocumentPaginationRequest {
    private List<Long> ids;
    private DocumentSortingRequest request;
}
