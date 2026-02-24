package com.thepowermisha.scheduler;

import com.thepowermisha.document.entity.Author;
import com.thepowermisha.document.repository.AuthorRepository;
import com.thepowermisha.document.repository.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.UUID;

@Component
@Lazy
public class AuthorUtil {

    private volatile Author author;
    private final Object lock = new Object();
    @Autowired
    private AuthorRepository authorRepository;

    private final String TECH_USER = "TECH_USER";

    private AuthorUtil(){}

    public Author getAuthor(){

        if(author == null){
            synchronized (lock){
                if( author == null){
                    this.author = authorRepository.findByName(TECH_USER).orElseThrow(IllegalStateException::new);
                }
            }
        }

        return author;
    }

}
