package com.microservice.user.entity;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;


@Component
public class UserCriteria {

    private static final Logger logger = LoggerFactory.getLogger(UserCriteria.class);
    private final MongoTemplate mongoTemplate;

    public UserCriteria(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public String getEmailById(String id) {
        logger.info("checking for object id {}",new ObjectId(id.trim()));
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(new ObjectId(id.trim())));
        query.fields().include("email").exclude("_id");

        var document = mongoTemplate.findOne(query, org.bson.Document.class, "user_entries");

        if (document != null) {
            String email = document.getString("email");
            logger.debug("✅ User Email found for ID {}: {}", id, email);
            return email;
        } else {
            logger.info("🔍 Looking up MongoDB for ID: '{}' (length: {})", id, id.length());
            return null;

        }
    }

    public UserEntity findByEmail(String email) {
        logger.info("finding email : {}",email);
        Query query = new Query();
        query.addCriteria(Criteria.where("email").is(email)); // exact match on field "email"
        return mongoTemplate.findOne(query, UserEntity.class,"user_entries");
    }
}
