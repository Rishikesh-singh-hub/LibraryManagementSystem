package com.microservice.library.controller;

import com.microservice.grpc.book.IssueBookResponse;
import com.microservice.library.dto.BookResponseDto;
import com.microservice.library.dto.IssueRequestDto;
import com.microservice.library.service.LibraryService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class LibraryController {

    Logger logger = LoggerFactory.getLogger(LibraryController.class);
    private final LibraryService libraryService;

    public LibraryController(LibraryService libraryService) {
        this.libraryService = libraryService;
    }

    @PostMapping("/issue")
    public ResponseEntity<?> issueBook(@Valid @RequestBody IssueRequestDto issueRequest, HttpServletRequest request){

        return libraryService.issueBook(issueRequest.getBid(),request);


    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PostMapping("/save-books")
    public ResponseEntity<List<BookResponseDto>> createBook(@Valid @RequestBody List<BookResponseDto> bookDto){
          libraryService.saveBook(bookDto);

        return ResponseEntity.ok().build();
    }


    @PostMapping("/return-books")
    public ResponseEntity<?> returnBooks(@Valid @RequestBody IssueRequestDto requestDto, HttpServletRequest req){
        return libraryService.returnBooks(requestDto,req);
    }


    @GetMapping("/issued-books")
    public ResponseEntity<?> allBooks(HttpServletRequest req){
        return libraryService.bookNotReturned(req);
    }

}
