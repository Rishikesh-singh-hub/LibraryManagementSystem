package com.microservice.user.jwt;

import com.microservice.user.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class LoginResponse {
    private UserDto userDto;
    private String jwtToken;





}
