package com.thepowermisha.document.controller;

import com.thepowermisha.document.security.UserContext;
import com.thepowermisha.document.security.UuidTokenUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import java.util.Objects;


public abstract class AbstractController {

    private UserContext userContext;

    @InitBinder()
    protected void initBinder(WebDataBinder webDataBinder, HttpServletRequest httpServletRequest) {
        String authorization = httpServletRequest.getHeader("Authorization");
        if (authorization != null && authorization.startsWith("Bearer ")) {
            userContext = new UserContext();
            userContext.setUserId(UuidTokenUtils.tokenToUuid(authorization.substring(7)));
        }
    }

    protected UserContext getUserContext(){
        return userContext;
    }

}
