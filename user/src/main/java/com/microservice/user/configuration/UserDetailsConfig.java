package com.microservice.user.configuration;

import com.microservice.user.entity.UserCriteria;
import com.microservice.user.entity.UserEntity;
import com.microservice.user.repository.UserRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class UserDetailsConfig implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(UserDetailsConfig.class);
    private final UserCriteria userCriteria;

    public UserDetailsConfig(UserCriteria userCriteria) {

        this.userCriteria = userCriteria;
    }

    @Override
    public UserDetails loadUserByUsername(String email)  {
        logger.info("loading email {}",email);
        UserEntity entity;
        entity = userCriteria.findByEmail(email);
        logger.info("user name = {}",entity.getUserName());
        return User.builder()
                .username(entity.getUserName())
                .password(entity.getPassword())
                .roles(entity.getRoles().stream()
                        .map(Enum::name)
                        .toArray(String[]::new))
                .build();


    }
}
