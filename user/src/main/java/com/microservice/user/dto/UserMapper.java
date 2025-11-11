package com.microservice.user.dto;

import com.microservice.grpc.user.UserResponse;
import com.microservice.grpc.user.UserResponseOrBuilder;
import com.microservice.user.entity.Role;
import com.microservice.user.entity.UserCriteria;
import com.microservice.user.entity.UserEntity;
import com.microservice.user.repository.UserRepo;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.swing.text.html.parser.Entity;
import java.util.List;

@Component
public class UserMapper {

    private final UserCriteria userCriteria;
    private final PasswordEncoder passwordEncoder;
    private final UserRepo userRepo;

    public UserMapper(UserCriteria userCriteria, PasswordEncoder passwordEncoder, UserRepo userRepo) {
        this.userCriteria = userCriteria;
        this.passwordEncoder = passwordEncoder;
        this.userRepo = userRepo;
    }

    public UserEntity toNewEntity(NewUserDto newUserDto){
        return UserEntity.builder()
                .userName(newUserDto.getUsername())
                .email(newUserDto.getEmail())
                .password(passwordEncoder.encode(newUserDto.getPassword()))
                .roles(List.of(Role.USER))
                .build();

    }

    public UserEntity toEntity(String email)  {
        return userCriteria.findByEmail(email);

    }

    public UserDto toDto(UserEntity entity){

        return UserDto.builder()
                .userName(entity.getUserName())
                .email(entity.getEmail())
                .booksIssuedCount(entity.getBooksIssuedCount())
                .build();

    }

    public UserDto toDto(UserLoginDto dto){
        String name = userCriteria.findByEmail(dto.getEmail()).getUserName();
        return UserDto.builder()
                .userName(name)
                .email(dto.getEmail())
                .build();


    }

    public UserResponse getUserResponseById(String id) {

         UserEntity entity = userRepo.findById(id).orElseThrow(()-> new UsernameNotFoundException("no user found"));
         UserResponse response = UserResponse.newBuilder()
                 .setId(entity.getId())
                 .setUsername(entity.getUserName())
                 .setEmail(entity.getEmail())
                 .setBooksIssuedCount(entity.getBooksIssuedCount())
                 .setMaxBooksAllowed(entity.getMaxBooksAllowed())
                 .setHasPendingFines(entity.isHasPendingFines())
                 .setCreatedDate(entity.getCreatedDate().toString())
                 .setActive(entity.isActive())
                 .build();

                    entity.getRoles().forEach(role -> response.toBuilder().addRoles(role.toString()));
        return response;
    }
}
