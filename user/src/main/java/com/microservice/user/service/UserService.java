package com.microservice.user.service;

import com.microservice.user.dto.*;
import com.microservice.user.entity.Role;
import com.microservice.user.entity.UserCriteria;
import com.microservice.user.entity.UserEntity;
import com.microservice.user.jwt.JwtUtils;
import com.microservice.user.jwt.LoginResponse;
import com.microservice.user.repository.UserRepo;
import jakarta.validation.Valid;
import org.hibernate.validator.internal.util.stereotypes.Lazy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    Logger logger = LoggerFactory.getLogger(UserService.class);
    private final JwtUtils jwtUtils;
    private final UserCriteria userCriteria;
    private final UserRepo userRepo;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authManager;

    UserService(PasswordEncoder passwordEncoder, JwtUtils jwtUtils, UserCriteria userCriteria, UserRepo userRepo, UserMapper userMapper, AuthenticationManager authManager){
        this.jwtUtils = jwtUtils;
        this.userCriteria = userCriteria;
        this.userRepo = userRepo;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.authManager = authManager;
    }


    public UserDto registerUser(NewUserDto newUserDto) throws DuplicateKeyException {
        UserEntity entity = userMapper.toNewEntity(newUserDto);
        userRepo.save(entity);
        logger.info("_id of saved entity: {}",entity.getId());
         return userMapper.toDto(entity);

    }

    public String getEmailById(String id){
        logger.info("service class checking for id");
        return userCriteria.getEmailById(id);

    }

    public UserDto updateUser(@Valid UserUpdateDto userUpdateDto, String token) {
        logger.info("user service updating user");
        String id = jwtUtils.getUserNameFromJwtToken(token);
        String email = getEmailById(id);
        UserEntity entity = userCriteria.findByEmail(email);
        entity.setUserName(userUpdateDto.getUsername());
        entity.setPassword(passwordEncoder.encode(userUpdateDto.getPassword()));
        userRepo.save(entity);

        return userMapper.toDto(entity);
    }

    public void createAdmin(){
        UserEntity admin = UserEntity.builder()
                        .userName("user")
                        .password(passwordEncoder.encode("user"))
                        .email("user@gmail.com")
                        .roles(List.of(Role.ADMIN,Role.USER))
                        .build();

        userRepo.save(admin);
    }

    public LoginResponse login(UserLoginDto userLoginDto) {
        logger.info("getting {} signed in", userLoginDto.getEmail());

        Authentication auth = null;
        try {
            auth = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userLoginDto.getEmail(), userLoginDto.getPassword())
            );
        } catch (Exception e) {
            logger.info(e.getMessage());
        }

        SecurityContextHolder.getContext().setAuthentication(auth);
        UserDetails userDetails = (UserDetails) auth.getPrincipal();

        logger.info("checking user account");
        UserEntity entity = userCriteria.findByEmail(userLoginDto.getEmail());
        logger.info("user books issued : {}",entity.getBooksIssuedCount());
        List<Role> roles = entity.getRoles();

        logger.info("user is valid {}",entity.getBooksIssuedCount() >= entity.getMaxBooksAllowed() || entity.isHasPendingFines());
        if (entity.getBooksIssuedCount() >= entity.getMaxBooksAllowed() || entity.isHasPendingFines()) {
            logger.info("user account suspended");

            roles.add(Role.SUSPENDED);
            entity.setRoles(roles);
            userRepo.save(entity);
        }
        logger.info("user verified");

        String jwtToken = jwtUtils.generateTokenFromId(userDetails, userLoginDto.getEmail());
        UserDto userDto = userMapper.toDto(entity);


        return new LoginResponse(userDto, jwtToken);

    }
}
