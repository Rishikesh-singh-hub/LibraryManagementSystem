package com.microservice.user.controller;

import com.microservice.user.dto.NewUserDto;
import com.microservice.user.dto.UserDto;
import com.microservice.user.dto.UserLoginDto;
import com.microservice.user.jwt.JwtUtils;
import com.microservice.user.jwt.LoginResponse;
import com.microservice.user.service.UserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/user")
public class PublicUserController {

    private final Logger logger = LoggerFactory.getLogger(PublicUserController.class);
    private final UserService userService;

    PublicUserController(UserService userService){
        this.userService = userService;
    }

    @GetMapping
    public String  healthCheck(){


        return "ok";
    }

    @GetMapping("/admin")
    public String  createAdmin(){
        userService.createAdmin();
        return "ok";
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody NewUserDto newUserDto){

        UserDto dto = userService.registerUser(newUserDto);

        return ResponseEntity.ok(dto);
    }




//    ------------------Check for in invalid Email id ---------X----------
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody UserLoginDto userLoginDto) throws Exception {
            logger.info("running");

            LoginResponse response = userService.login(userLoginDto) ;
            logger.debug("Login Successful");
            return ResponseEntity.ok(response);


    }
}
