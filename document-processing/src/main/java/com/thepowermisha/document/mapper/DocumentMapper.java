package com.thepowermisha.document.mapper;

import com.thepowermisha.document.dto.DocumentDto;
import com.thepowermisha.document.entity.Document;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DocumentMapper {
    DocumentDto toDto(Document document);

    @Mapping(target = "documentHistory", ignore = true)
    @Mapping(target = "author", ignore = true)
    Document toEntity(DocumentDto documentDto);
}
