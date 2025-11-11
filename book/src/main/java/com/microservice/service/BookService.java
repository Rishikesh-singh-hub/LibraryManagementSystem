//package com.microservice.service;
//
//import com.microservice.dto.BookDto;
//import com.microservice.dto.BookMapper;
//import com.microservice.entity.BookEntity;
//import com.microservice.repository.BookRepo;
//import jakarta.validation.Valid;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//public class BookService {
//    Logger logger = LoggerFactory.getLogger(BookService.class);
//
//    private final BookRepo bookRepo;
//    private final BookMapper bookMapper;
//
//    public BookService(BookRepo bookRepo, BookMapper bookMapper) {
//        this.bookRepo = bookRepo;
//        this.bookMapper = bookMapper;
//    }
//
//
//    public List<BookDto> saveBook(@Valid List<BookDto> bookDto) {
//
//        List<BookEntity> entities = bookDto.stream().map(
//                bookMapper::toEntity
//        ).toList();
//        List<BookEntity> saved =  bookRepo.saveAll(entities);
//        logger.info("{} book added",saved.size());
//        return saved.stream().map(
//                bookMapper::toDto
//        ).toList();
//
//    }
//
//    public List<BookDto> getAllBooks() {
//
//        List<BookEntity> entities = bookRepo.findAll();
//
//        return entities.stream().map(
//                bookMapper::toDto).toList();
//    }
//}
