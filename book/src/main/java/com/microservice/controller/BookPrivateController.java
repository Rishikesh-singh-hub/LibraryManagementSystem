//package com.microservice.controller;
//
//import com.microservice.dto.BookDto;
//import com.microservice.service.BookService;
//import jakarta.validation.Valid;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//
//@RestController
//@RequestMapping("/auth/book")
//public class BookPrivateController {
//
//    private final BookService bookService;
//
//    public BookPrivateController(BookService bookService) {
//        this.bookService = bookService;
//    }
//
//    @GetMapping
//    public String healthCheck(){
//        return "Ok";
//    }
//
//    @PostMapping
//    public ResponseEntity<List<BookDto>> createBook(@Valid @RequestBody List<BookDto> bookDto){
//        List<BookDto> dtos =   bookService.saveBook(bookDto);
//        if (dtos!= null)
//            return ResponseEntity.ok(dtos);
//        return ResponseEntity.noContent().build();
//    }
//
//
//
//}
