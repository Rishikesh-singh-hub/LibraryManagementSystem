package com.microservice.user.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Document(collection = "user_entries")
@Component
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder(toBuilder = true)

public class UserEntity {

    @Id
    private String id;

    private String userName;
    private String email;
    private String password;

    private List<Role> roles;

    // --- NEW FIELDS ---
    @Builder.Default
    private int booksIssuedCount = 0;        // quick lookup
    @Builder.Default
    private int maxBooksAllowed = 3;         // configurable rule
    @Builder.Default
    private boolean hasPendingFines = false; // eligibility flag
    @Builder.Default
    private LocalDate createdDate = LocalDate.now();
    @Builder.Default
    private boolean active = true;

}
