package com.microservice.library.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.List;

@Component
public class JwtUtils {

Logger logger = LoggerFactory.getLogger(JwtUtils.class);
    @Value("${spring.app.jwtSecret}")
    private String jwtSecret;

    public boolean validateToken(String jwt) {

        logger.info("validating jwt ");


        Jwts.parser()
                .verifyWith((SecretKey)key())
                .build()
                .parseSignedClaims(jwt);

        return true;

    }

    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    public List<String> getRolesFromJwt(String jwt) {

        Claims claims =Jwts.parser()
                .verifyWith((SecretKey) key())
                .build()
                .parseSignedClaims(jwt)
                .getPayload();
        return (List<String>) claims.get("roles");
    }

    public String getUserIdFromJwt(String jwt){

       Claims claim =  Jwts.parser()
                .verifyWith((SecretKey) key())
                .build()
                .parseSignedClaims(jwt)
                .getPayload();
        return claim.getSubject();
    }
}
