package com.microservice.library.dto;

import com.microservice.library.entity.Status;
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
public class BookResponseDto {


    @NotBlank(message = "Book title cannot be blank")
    private String title;

    @NotBlank(message = "Author name cannot be blank")
    private String author;

    @Builder.Default
    private String genre = "General";         // fallback if not specified

    @Builder.Default
    private String publisher = "Unknown";     // default when publisher isn’t provided



    private Integer totalCopies ;          // start with at least one copy


    private Integer availableCopies;      // initially all copies are available


    @Builder.Default
    private String location = "Main Shelf";   // logical default location


    private String status ;         // default operational state


}

