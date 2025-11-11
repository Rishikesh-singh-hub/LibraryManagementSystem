package com.microservice.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Builder
public class UserDto {

    private String userName;
    private String email;

    // --- NEW FIELDS ---
    @Builder.Default
    private int booksIssuedCount = 0;        // quick lookup
    @Builder.Default
    private int maxBooksAllowed = 3;         // configurable rule
    @Builder.Default
    private boolean hasPendingFines = false; // eligibility flag
    @Builder.Default
    private LocalDate createdDate = LocalDate.now();


}
