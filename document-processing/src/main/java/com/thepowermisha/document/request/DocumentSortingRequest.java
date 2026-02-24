package com.thepowermisha.document.request;

import com.thepowermisha.document.type.DocumentSortingField;
import com.thepowermisha.document.type.SortDirection;
import lombok.Data;

@Data
public class DocumentSortingRequest {
    private Integer page = 0;
    private Integer size = 20;

    private DocumentSortingField sortBy = DocumentSortingField.ID;
    private SortDirection direction = SortDirection.ASC;

    public void setSortBy(String field){
        this.sortBy = DocumentSortingField.getByFieldName(field);
    }

    public void setDirection(String direction) {
        this.direction = SortDirection.getByFieldName(direction);
    }
}
