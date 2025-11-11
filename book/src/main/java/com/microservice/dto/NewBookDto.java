package com.microservice.dto;

import com.microservice.entity.BookStatus;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class NewBookDto {


    @NotBlank(message = "Book title cannot be blank")
    private String title;

    @NotBlank(message = "Author name cannot be blank")
    private String author;

    @Builder.Default
    private String genre = "General";         // fallback if not specified

    @Builder.Default
    private String publisher = "Unknown";     // default when publisher isn’t provided

    @Builder.Default
    @NotNull(message = "Publication year cannot be null")
    private Integer publicationYear = LocalDateTime.now().getYear();

    @Builder.Default
    @Min(value = 1, message = "Total copies must be at least 1")
    private Integer totalCopies = 1;          // start with at least one copy

    @Builder.Default
    @Min(value = 0, message = "Available copies cannot be negative")
    private Integer availableCopies = 1;      // initially all copies are available


    @Builder.Default
    private String location = "Main Shelf";   // logical default location

    @Builder.Default
    private BookStatus status = BookStatus.ACTIVE;         // default operational state


}
