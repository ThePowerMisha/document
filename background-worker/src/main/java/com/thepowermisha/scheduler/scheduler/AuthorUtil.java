package com.thepowermisha.scheduler.scheduler;

import com.thepowermisha.document.entity.Author;
import com.thepowermisha.document.repository.AuthorRepository;
import com.thepowermisha.document.security.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
@Lazy
public class AuthorUtil {

    private volatile UserContext userContext;
    private final Object lock = new Object();
    @Autowired
    private AuthorRepository authorRepository;

    public UserContext getUserContext(){

        if(userContext == null){
            synchronized (lock){
                if( userContext == null){
                    Author user = authorRepository.findTechUser()
                            .orElseThrow(() -> new IllegalStateException("No tech user found"));
                    this.userContext = new UserContext(user.getId());
                }
            }
        }

        return userContext;
    }

}
