package com.microservice.library.entity;

import com.microservice.grpc.book.BookListResponse;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import com.microservice.library.entity.LibraryRecord;

import java.util.List;

@Component
public class LibraryCriteria {

    private static final Logger logger = LoggerFactory.getLogger(LibraryCriteria.class);
    private final MongoTemplate mongoTemplate;

    public LibraryCriteria(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }



    public LibraryRecord getRecordById(String userId,String bookId) {
        logger.info("checking for userId with bookId {} , {}", userId.trim(), bookId.trim());
        Query query = new Query();
        query.addCriteria(Criteria.where("userId").is(userId.trim())
                .and("bookId").is(bookId.trim())
                .and("returned").is(false)
        );



        var document = mongoTemplate.findOne(query, org.bson.Document.class, "library_records");

        if (document == null) {
            logger.info("🔍 No user record found for ID");
            return null;



        }
        LibraryRecord libraryRecord = mongoTemplate.getConverter().read(LibraryRecord.class, document);
        logger.debug("✅ User record found for ID {}", libraryRecord.getUserId());

        return libraryRecord;
    }

    public List<String> getRecordById(String userId) {
        logger.info("Fetching all unreturned records for userId {}", userId.trim());

        Query query = new Query(Criteria.where("userId").is(userId.trim())
                .and("returned").is(false));
        query.fields().include("bookId");


        List<String> ids = mongoTemplate.findDistinct(
                query, "bookId", "library_records", String.class);


        logger.info("Found {} unreturned books for userId {}", ids.size(), userId);
        return ids;
    }
}

