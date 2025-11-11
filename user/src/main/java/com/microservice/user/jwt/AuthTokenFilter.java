package com.microservice.user.jwt;

import com.microservice.user.configuration.UserDetailsConfig;
import com.microservice.user.exception.ErrorResponse;
import com.microservice.user.service.UserService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Lazy
public class AuthTokenFilter extends OncePerRequestFilter {
    @Autowired
    private JwtUtils jwtUtils;

    private final UserDetailsConfig userDetailsConfig;

    private final UserService userService;

    private static final Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);

    public AuthTokenFilter(UserDetailsConfig userDetailsConfig, UserService userService) {
        this.userDetailsConfig = userDetailsConfig;
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        logger.info("🔥 AuthTokenFilter is running");
        try {
            String jwt = parseJwt(request);
            logger.info("checking jwt {}",jwt);
            if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
                logger.info("jwt validated");
                String id = jwtUtils.getUserNameFromJwtToken(jwt);
                logger.info("got id from jwt {}",id);
                logger.info("user email by id {}",userService.getEmailById(id));
                UserDetails userDetails = userDetailsConfig.loadUserByUsername(userService.getEmailById(id));

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userDetails,
                                null,
                                userDetails.getAuthorities());

                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);

            }
        } catch (ExpiredJwtException e) {
            writeErrorResponse(response, new ErrorResponse("Token expired. Please login again.", HttpStatus.UNAUTHORIZED.value()), HttpStatus.UNAUTHORIZED);
        } catch (MalformedJwtException | UnsupportedJwtException | IllegalArgumentException e) {
            writeErrorResponse(response, new ErrorResponse("Invalid token.", HttpStatus.UNAUTHORIZED.value()), HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            writeErrorResponse(response, new ErrorResponse("Authentication failed.", HttpStatus.INTERNAL_SERVER_ERROR.value()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        filterChain.doFilter(request, response);

    }

    private String parseJwt(HttpServletRequest request) {
        String jwt = jwtUtils.getJwtFromHeader(request);
        logger.debug("AuthTokenFilter.java: {}", jwt);
        return jwt;
    }

    private void writeErrorResponse(HttpServletResponse response, ErrorResponse error, HttpStatus status) throws IOException {
        response.setStatus(status.value());
        response.setContentType("application/json");
        response.getWriter().write(
                String.format("{\"message\":\"%s\",\"status\":%d}", error.getMessage(), error.getStatus())
        );
    }
}
