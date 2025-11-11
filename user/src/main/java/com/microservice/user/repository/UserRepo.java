package com.microservice.user.repository;

import com.microservice.user.entity.UserEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepo extends MongoRepository<UserEntity,String> {

    public boolean existsByEmail(String email);
}
