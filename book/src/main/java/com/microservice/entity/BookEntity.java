package com.microservice.entity;


import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Document(collection = "book_entries")
public class BookEntity {
    @Id
    private String id;

    private String title;
    private String author;

    @Builder.Default
    private String genre = "General";         // fallback if not specified

    @Builder.Default
    private String publisher = "Unknown";     // default when publisher isn’t provided


    private int totalCopies ;              // start with at least one copy

    private int availableCopies ;          // initially all copies are available

    @Builder.Default
    private String location = "Main Shelf";   // logical default location

    @Builder.Default
    private BookStatus status = BookStatus.ACTIVE;
    // default operational state

}
