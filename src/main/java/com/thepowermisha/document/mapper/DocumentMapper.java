package com.thepowermisha.document.mapper;

import com.thepowermisha.document.dto.DocumentDto;
import com.thepowermisha.document.entity.Document;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DocumentMapper {
    DocumentDto toDto(Document document);

    Document toEntity(DocumentDto documentDto);
}
