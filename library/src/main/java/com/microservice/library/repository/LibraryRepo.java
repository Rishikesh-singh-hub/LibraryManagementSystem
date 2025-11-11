package com.microservice.library.repository;

import com.microservice.library.entity.LibraryRecord;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface LibraryRepo extends MongoRepository<LibraryRecord,String> {
}
