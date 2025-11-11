package com.microservice.library.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Getter
@Setter
@Document(collection = "library_records")
@Builder
public class LibraryRecord {

    @Id
    private String id;

    private String userId;    // fetched from User Service (via gRPC)
    private String bookId;    // fetched from Book Service (via gRPC)

    @Builder.Default
    private LocalDate issueDate = LocalDate.now();
    @Builder.Default
    private LocalDate returnDate =LocalDate.now().plusDays(7);
    @Builder.Default
    private boolean returned = false;

    private String status; // ISSUED, RETURNED, ARCHIVED


}