package com.microservice.repository;

import com.microservice.entity.BookEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class BookCriteria {

    Logger logger = LoggerFactory.getLogger(BookCriteria.class);
    private final MongoTemplate mongoTemplate;

    public BookCriteria(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public List<BookEntity> getAvailBooks() {

        logger.info("Checking for available books");
        Query query = new Query(Criteria.where("availableCopies").gte(1));
        List<BookEntity> bookEntities = mongoTemplate.find(query, BookEntity.class, "book_entries");

        if (bookEntities.isEmpty()) {
            logger.info("🔍 No available books found");
            return Collections.emptyList();
        }

        logger.debug("✅ Found {} available books", bookEntities.get(1).getAvailableCopies());
        return bookEntities;

    }
}
