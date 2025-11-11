package com.microservice.library.dto;

import java.time.LocalDate;

public class LibraryDto {

    private String userId;    // fetched from User Service (via gRPC)
    private String bookId;    // fetched from Book Service (via gRPC)

    private LocalDate issueDate;
    private LocalDate dueDate;
    private LocalDate returnDate;

    private String status;

}
