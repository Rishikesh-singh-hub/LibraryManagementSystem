package com.microservice.dto;

import com.microservice.entity.BookStatus;
import com.microservice.grpc.book.BookResponse;
import com.microservice.entity.BookEntity;
import com.microservice.grpc.book.CreateBookRequest;
import com.microservice.service.BookGrpcServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


@Component
public class BookMapper {
    Logger logger = LoggerFactory.getLogger(BookMapper.class);
    public BookResponse toBookResponse(BookEntity entity){

        return BookResponse.newBuilder()
                .setId(entity.getId())
                .setTitle(entity.getTitle())
                .setAuthor(entity.getAuthor())
                .setGenre(entity.getGenre())
                .setPublisher(entity.getPublisher())
                .setTotalCopies(entity.getTotalCopies())
                .setAvailableCopies(entity.getAvailableCopies())
                .setLocation(entity.getLocation())
                .setStatus(entity.getStatus().toString())
                .build();


    }


    public BookEntity toEntity(CreateBookRequest request) {

        BookStatus status = BookStatus.valueOf(request.getStatus());

        return BookEntity.builder()
                .title(request.getTitle())
                .author(request.getAuthor())
                .genre(request.getGenre())
                .publisher(request.getPublisher())
                .totalCopies(request.getTotalCopies())
                .availableCopies(request.getAvailableCopies())
                .location(request.getLocation())
                .status(status)
                .build();


    }


}
