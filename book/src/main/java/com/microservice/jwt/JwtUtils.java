//package com.microservice.jwt;
//
//import io.jsonwebtoken.*;
//import io.jsonwebtoken.io.Decoders;
//import io.jsonwebtoken.security.Keys;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//
//import javax.crypto.SecretKey;
//import java.security.Key;
//import java.util.List;
//
//@Component
//public class JwtUtils {
//
//    Logger logger =  LoggerFactory.getLogger(JwtUtils.class);
//    @Value("${spring.app.jwtSecret}")
//    private String jwtSecret;
//
//    public boolean validate(String jwt)
//            throws MalformedJwtException, ExpiredJwtException, UnsupportedJwtException, IllegalArgumentException{
//        logger.info("validating jwt ");
//        Jwts.parser()
//                .verifyWith((SecretKey) key())
//                .build()
//                .parseSignedClaims(jwt);
//        return true;
//
//
//
//    }
//
//    private Key key() {
//        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
//    }
//
//    public List<String> getUserRolesFromJwtToken(String jwt) {
//
//        Claims claim =  Jwts.parser()
//                .verifyWith((SecretKey) key())
//                .build()
//                .parseSignedClaims(jwt)
//                .getPayload();
//        return (List<String>) claim.get("roles");
//    }
//}
