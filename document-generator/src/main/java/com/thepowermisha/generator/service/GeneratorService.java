package com.thepowermisha.generator.service;

import com.thepowermisha.document.security.UuidTokenUtils;
import com.thepowermisha.generator.util.AuthorUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Service
@RequiredArgsConstructor
@Slf4j
public class GeneratorService {
    private final AuthorUtil authorUtil;

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${app.document-count:100}")
    private Integer documentCount;
    @Value("${app.api-url:http://localhost:8080/api/document}")
    private String url;

    public void generateDocuments(){
        log.info("Document to creation " + documentCount);
        for (int i = 0; i < documentCount; i++) {

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + UuidTokenUtils.uuidToToken(authorUtil.getAuthor().getId()));
            headers.setContentType(MediaType.APPLICATION_JSON);

            String requestBody = """
                    {
                      "title": "Generated document %d"
                    }
                    """.formatted(i + 1);

            HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.PUT,
                    entity,
                    String.class
            );

            log.info("Created document #" + (i + 1) +
                    "/" + documentCount +
                    " Status: " + response.getStatusCode());
        }
    }

}
