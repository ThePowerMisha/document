package com.thepowermisha.document.service;

import com.thepowermisha.document.entity.Author;
import com.thepowermisha.document.repository.AuthorRepository;
import com.thepowermisha.document.security.UuidTokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthorService {

    private final AuthorRepository authorRepository;

    public String getAuthorToken(String authorName){
        return UuidTokenUtils.uuidToToken(authorRepository.findByName(authorName)
                        .orElseThrow()
                        .getId());
    }

    public List<String> getAuthorsNames(){
        return authorRepository.findAll().stream()
                .map(Author::getName)
                .toList();
    }

}
