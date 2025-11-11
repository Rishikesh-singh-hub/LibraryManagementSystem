package com.microservice.library.dto;

import com.microservice.grpc.book.BookResponse;
import com.microservice.grpc.book.BookStatus;
import com.microservice.grpc.book.CreateBookRequest;
import com.microservice.grpc.user.UserResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

@Component
public class LibraryMapper {


    Logger logger = LoggerFactory.getLogger(LibraryMapper.class);
    public LibraryBookDto toLibraryBookDto(BookResponse entity){


        return LibraryBookDto.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .author(entity.getAuthor())
                .genre(entity.getGenre())
                .publisher(entity.getPublisher())
                .availableCopies(entity.getAvailableCopies())
                .location(entity.getLocation())
                .status(entity.getStatus())
                .build();


    }

    public BookResponseDto toBookResponseDto(LibraryBookDto dto) {

        return   BookResponseDto.builder()
                .title(dto.getTitle())
                .author(dto.getAuthor())
                .genre(dto.getGenre())
                .publisher(dto.getPublisher())
                .availableCopies(dto.getAvailableCopies())
                .location(dto.getLocation())
                .status(dto.getStatus())
                .build();

    }

    public UserResponseDto toUserResponseDto(UserResponse updatedResponse) {

        return UserResponseDto.builder()
                .username(updatedResponse.getUsername())
                .email(updatedResponse.getEmail())
                .booksIssuedCount(updatedResponse.getBooksIssuedCount())
                .maxBooksAllowed(updatedResponse.getMaxBooksAllowed())
                .hasPendingFines(updatedResponse.getHasPendingFines())
                .createdDate(updatedResponse.getCreatedDate())
                .active(updatedResponse.getActive())
                .build();


    }

    public BookResponseDto toBookResponseDto(BookResponse dto) {

        return   BookResponseDto.builder()
                .title(dto.getTitle())
                .author(dto.getAuthor())
                .genre(dto.getGenre())
                .publisher(dto.getPublisher())
                .totalCopies(dto.getTotalCopies())
                .location(dto.getLocation())
                .status(dto.getStatus())
                .build();

    }

    public CreateBookRequest toCreateBookObj(BookResponseDto dto) {


        return CreateBookRequest.newBuilder()
                .setTitle(dto.getTitle())
                .setAuthor(dto.getAuthor())
                .setGenre(dto.getGenre())
                .setPublisher(dto.getPublisher())
                .setTotalCopies(dto.getTotalCopies())
                .setAvailableCopies(dto.getAvailableCopies())
                .setLocation(dto.getLocation())
                .setStatus(dto.getStatus())
                .build();

    }
}

