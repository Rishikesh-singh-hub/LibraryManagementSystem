package com.microservice.user.controller;

import com.microservice.user.dto.UserDto;
import com.microservice.user.dto.UserLoginDto;
import com.microservice.user.dto.UserUpdateDto;
import com.microservice.user.service.UserService;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class PrivateUserController {
    Logger logger = LoggerFactory.getLogger(PrivateUserController.class);

    private final UserService userService;

    public PrivateUserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String healthCheck(){
        return "ok";
    }

    @PostMapping("/update")
    public ResponseEntity<?> updateUser(@Valid @RequestBody UserUpdateDto userUpdateDtoDto, HttpServletRequest request){
        logger.info("update Controller working");
        UserDto dto = null;
        String authHeader = request.getHeader("Authorization");

        if(authHeader !=null && authHeader.startsWith("Bearer ")){
            String token = authHeader.substring(7);
            dto = userService.updateUser(userUpdateDtoDto,token);

        }

        if (dto != null) {
            logger.info("dto {} ", dto.getEmail());
            return ResponseEntity.ok(dto);
        }
        return ResponseEntity.badRequest().build();
    }

}
