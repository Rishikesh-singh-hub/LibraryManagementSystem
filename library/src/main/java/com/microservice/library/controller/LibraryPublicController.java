package com.microservice.library.controller;


import com.microservice.library.dto.BookResponseDto;
import com.microservice.library.service.LibraryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/public")
public class LibraryPublicController {

    private final LibraryService libraryService;

    public LibraryPublicController(LibraryService libraryService) {
        this.libraryService = libraryService;
    }

    @GetMapping("/health-check")
    public String healthCheck(){
        return "healthy";
    }

    @GetMapping("/all-books")
    public ResponseEntity<?> allBooks(){

        return libraryService.getAllBooks();


    }
    
}
