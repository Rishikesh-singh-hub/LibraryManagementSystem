//package com.microservice.controller;
//
//
//import com.microservice.dto.BookDto;
//import com.microservice.service.BookService;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/book")
//public class BookPublicController {
//
//    private final BookService bookService;
//
//    public BookPublicController(BookService bookService) {
//        this.bookService = bookService;
//    }
//
//    @GetMapping
//    public String healthCheck(){
//        return "ok";
//    }
//
//    @GetMapping("all-books")
//    public ResponseEntity<?> getAllBooks(){
//
//        List<BookDto> dtos = bookService.getAllBooks();
//        if(dtos != null)
//            return ResponseEntity.ok(dtos);
//        return  ResponseEntity.ofNullable("No books found");
//    }
//
//
//
//}
