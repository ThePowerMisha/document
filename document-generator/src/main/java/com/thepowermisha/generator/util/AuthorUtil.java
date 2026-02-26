package com.thepowermisha.generator.util;

import com.thepowermisha.document.entity.Author;
import com.thepowermisha.document.repository.AuthorRepository;
import com.thepowermisha.document.security.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
@Lazy
public class AuthorUtil {

    private volatile Author author;
    private final Object lock = new Object();
    @Autowired
    private AuthorRepository authorRepository;

    public Author getAuthor(){

        if(author == null){
            synchronized (lock){
                if( author == null){
                    this.author = authorRepository.findTechUser()
                            .orElseThrow(() -> new IllegalStateException("No tech user found"));
                }
            }
        }

        return author;
    }

}
