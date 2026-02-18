package com.thepowermisha.document.service;

import com.thepowermisha.document.dto.AuthorDto;
import com.thepowermisha.document.repository.AuthorRepository;
import com.thepowermisha.document.security.UuidTokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthorService {

    private final AuthorRepository authorRepository;

    public String getAuthorToken(String authorName){
        return UuidTokenUtils.uuidToToken(authorRepository.findByName(authorName)
                        .orElseThrow()
                        .getId());
    }

}
