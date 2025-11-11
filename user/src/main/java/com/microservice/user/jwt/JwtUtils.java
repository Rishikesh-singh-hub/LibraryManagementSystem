package com.microservice.user.jwt;

import com.microservice.user.dto.UserMapper;
import com.microservice.user.entity.UserEntity;
import com.microservice.user.exception.ErrorResponse;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.IOException;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtils {

    Logger logger = LoggerFactory.getLogger(JwtUtils.class);
    @Value("${spring.app.jwtSecret}")
    private String jwtSecret;

    private final UserMapper userMapper;

    public JwtUtils(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public String generateTokenFromId(UserDetails userDetails,String email) throws UsernameNotFoundException {

        UserEntity entity = userMapper.toEntity(email);
        logger.info("id to create jwt: {}",entity.getId());
        return Jwts.builder()
                .subject(entity.getId())
                .claim("roles",entity.getRoles())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis()+1000*60*60))
                .signWith(key())
                .compact();

    }

    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    public boolean validateJwtToken(String jwt)
            throws MalformedJwtException, ExpiredJwtException, UnsupportedJwtException, IllegalArgumentException {
        Jwts.parser()
                .verifyWith((SecretKey) key())
                .build()
                .parseSignedClaims(jwt);
        return true;
    }

    public String getUserNameFromJwtToken(String jwt) {

            return Jwts.parser()
                    .verifyWith((SecretKey) key())
                    .build().parseSignedClaims(jwt)
                    .getPayload().getSubject();

    }

    public String getJwtFromHeader(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7); // Remove Bearer prefix
        }
        return null;
    }



}
