package com.microservice.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class UserMicroserviceApplication {
    static Logger logger = LoggerFactory.getLogger(UserMicroserviceApplication.class);
	public static void main(String[] args) {
        logger.info("refine exception mapping for LibraryNotFound and BookUnavailable after library module integration");
        logger.info("Roles not showing properly in issuing and returning books ");
		SpringApplication.run(UserMicroserviceApplication.class, args);

	}

}
