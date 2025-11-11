package com.microservice.repository;

import com.microservice.entity.BookEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BookRepo extends MongoRepository<BookEntity, String> {
}
