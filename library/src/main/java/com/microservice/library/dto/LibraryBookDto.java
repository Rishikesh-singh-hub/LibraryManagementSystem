package com.microservice.library.dto;

import com.microservice.library.entity.Status;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Document(collection = "book_entries")
public class LibraryBookDto {
    @Id
    private String id;

    private String title;
    private String author;


    private String genre ;         // fallback if not specified

    private String publisher ;     // default when publisher isn’t provided

    private int availableCopies;          // initially all copies are available

    @Builder.Default
    private String location = "Main Shelf";   // logical default location

    private String status ;
    // default operational state

}

