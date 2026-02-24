package com.thepowermisha.document.type;

import lombok.Getter;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
public enum DocumentSortingField {
    ID("id"),
    NAME("name"),
    STATUS("status"),
    CREATED("createdAt"),
    UPDATED("updatedAt");

    private final String fieldName;

    private static final Map<String, DocumentSortingField> helper = Stream.of(DocumentSortingField.values())
            .collect(Collectors.toMap(DocumentSortingField::getFieldName, v -> v));

    private DocumentSortingField(String fieldName) {
        this.fieldName = fieldName;
    }

    public static DocumentSortingField getByFieldName(String fn) {
        return helper.get(fn);
    }
}

