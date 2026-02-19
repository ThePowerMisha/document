package com.thepowermisha.document.controller;

import com.thepowermisha.document.service.AuthorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AuthorController {
    private final AuthorService authorService;

    @GetMapping("/{name}/token")
    public String getAuthorToken(@PathVariable String name){
        return authorService.getAuthorToken(name);
    }

    @GetMapping("/get-authors-names")
    public List<String> getAuthorsNames(){
        return authorService.getAuthorsNames();
    }
}
