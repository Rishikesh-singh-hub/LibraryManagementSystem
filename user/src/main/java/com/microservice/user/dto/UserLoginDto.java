package com.microservice.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class UserLoginDto {

    @NotBlank(message = "valid Email id is required")
    @Email
    private String email;
    @NotBlank(message = "Password is required")
    private String password;


}
