package com.microservice.library.dto;

import com.microservice.library.entity.Role;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class UserResponseDto {

    private String username;
    private String email;
    private int booksIssuedCount;
    private int maxBooksAllowed;
    private boolean hasPendingFines;
    private String createdDate;
    private boolean active;

}
