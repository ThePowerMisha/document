package com.thepowermisha.scheduler.document;


//@SpringBootTest(classes = DocumentServiceApplication.class)
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class DocumentServiceTest {

//    @Autowired
//    private DocumentService documentService;
//    @Autowired
//    private DocumentRepository documentRepository;
//    @Autowired
//    private AuthorRepository authorRepository;
//    @Autowired
//    private DocumentTransactionalService documentTransactionalService;

//    @Test
//    @Transactional
//    void addDocument() {
//        Author author = new Author();
//        author.setName("Author for Test");
//
////        Author dbAuthor = authorRepository.save(author);
//
//        Author dbAuthor = authorRepository.findById(UUID.fromString("bb7f5228-7eea-437d-80bb-83e5108743ae")).orElseThrow();
//
//        DocumentDto savedDocument = documentService.createDraft(dbAuthor.getId(), "TestDocument.docx");
//
//        Document dbDocument = documentRepository.findById(savedDocument.getId()).orElseThrow();
//
//        Assertions.assertThat(savedDocument)
//                .usingRecursiveComparison()
//                .isEqualTo(dbDocument);
//    }
//
//    @Test
//    @Transactional
//    void submitDocuments() {
//
//        Author author = new Author();
//        author.setName("Author for Test");
//
////        Author dbAuthor = authorRepository.save(author);
//
//        Author dbAuthor = authorRepository.findById(UUID.fromString("bb7f5228-7eea-437d-80bb-83e5108743ae")).orElseThrow();
//
//        List<Document> documentList = IntStream.range(0, 20)
//                .mapToObj(i -> new Document()
//                    .setDocumentNumber(DocumentNumberGenerator.generate())
//                    .setAuthor(dbAuthor)
//                    .setName("docTest" + i)
//                    .setStatus(DocumentStatus.DRAFT)
//                    .setCreatedAt(ZonedDateTime.now())
//                    .setUpdatedAt(ZonedDateTime.now())
//                ).toList();
//
//        documentService.createDraft(dbAuthor.getId(), "TestDocument.docx");
//
//        List<Long> documenIds = documentRepository.saveAll(documentList)
//                .stream()
//                .map(Document::getId)
//                .toList();
//
//
//        documentService.submitDocuments(dbAuthor.getId(), documenIds);
//
//
//
//    }

//@Autowired
//private DataSource dataSource;
//
//    @Test
//    void checkDb() throws Exception {
//        System.out.println(dataSource.getConnection().getMetaData().getURL());
//    }

//    @Test
//    void concurrentTest(){
//
//        Author author = new Author();
//        author.setName("Author for Test");
//
////        Author dbAuthor = authorRepository.save(author);
//
//        Author dbAuthor = authorRepository.findById(UUID.fromString("bb7f5228-7eea-437d-80bb-83e5108743ae")).orElseThrow();
//
//        DocumentDto savedDocument = documentService.createDraft(dbAuthor.getId(), "TestDocument.docx");
//
//        Document dbDocument = documentRepository.findById(savedDocument.getId()).orElseThrow();
//
//        List<DocumentProcessedStatusDto> processedStatusDtos = documentService.submitDocuments(dbAuthor.getId(), List.of(dbDocument.getId()));
//
//        Map<DocumentResultStatus, Integer> concurrentResult = documentService.concurrentApprove(5, 100, dbDocument.getId(), dbAuthor);
//
//        Assertions.assertThat(concurrentResult.get(DocumentResultStatus.SUCCESS)).isEqualTo(1);
//        Assertions.assertThat(concurrentResult.get(DocumentResultStatus.CONFLICT)).isEqualTo(99);
//    }

}
