package com.thepowermisha.document.type;

import lombok.Getter;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
public enum SortDirection {
    ASC("asc"),
    DESC("desc");

    private final String fieldName;

    private static final Map<String, SortDirection> helper = Stream.of(SortDirection.values())
            .collect(Collectors.toMap(SortDirection::getFieldName, v -> v));

    private SortDirection(String fieldName) {
        this.fieldName = fieldName;
    }

    public static SortDirection getByFieldName(String fn) {
        return helper.get(fn.toLowerCase());
    }
}
