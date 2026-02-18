package com.thepowermisha.document;


import com.thepowermisha.document.dto.DocumentDto;
import com.thepowermisha.document.entity.Author;
import com.thepowermisha.document.entity.Document;
import com.thepowermisha.document.repository.AuthorRepository;
import com.thepowermisha.document.repository.DocumentRepository;
import com.thepowermisha.document.service.DocumentService;
import com.thepowermisha.document.type.DocumentStatus;
import com.thepowermisha.document.util.DocumentNumberGenerator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.IntStream;

public class DocumentServiceTest extends TestBase {

    @Autowired
    private DocumentService documentService;
    @Autowired
    private DocumentRepository documentRepository;
    @Autowired
    private AuthorRepository authorRepository;

    @Test
    @Transactional
    void addDocument() {
        Author author = new Author();
        author.setName("Author for Test");

//        Author dbAuthor = authorRepository.save(author);

        Author dbAuthor = authorRepository.findById(UUID.fromString("bb7f5228-7eea-437d-80bb-83e5108743ae")).orElseThrow();

        DocumentDto savedDocument = documentService.createDraft(dbAuthor.getId(), "TestDocument.docx");

        Document dbDocument = documentRepository.findById(savedDocument.getId()).orElseThrow();

        Assertions.assertThat(savedDocument)
                .usingRecursiveComparison()
                .isEqualTo(dbDocument);
    }

    @Test
    @Transactional
    void submitDocuments() {

        Author author = new Author();
        author.setName("Author for Test");

//        Author dbAuthor = authorRepository.save(author);

        Author dbAuthor = authorRepository.findById(UUID.fromString("bb7f5228-7eea-437d-80bb-83e5108743ae")).orElseThrow();

        List<Document> documentList = IntStream.range(0, 20)
                .mapToObj(i -> new Document()
                    .setDocumentNumber(DocumentNumberGenerator.generate())
                    .setAuthor(dbAuthor)
                    .setName("docTest" + i)
                    .setStatus(DocumentStatus.DRAFT)
                    .setCreatedAt(ZonedDateTime.now())
                    .setUpdatedAt(ZonedDateTime.now())
                ).toList();

        documentService.createDraft(dbAuthor.getId(), "TestDocument.docx");

        List<Long> documenIds = documentRepository.saveAll(documentList)
                .stream()
                .map(Document::getId)
                .toList();


        documentService.submitDocuments(dbAuthor.getId(), documenIds);



    }

}
