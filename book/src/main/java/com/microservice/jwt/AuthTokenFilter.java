//package com.microservice.jwt;
//
//import com.microservice.exception.ErrorResponse;
//import io.jsonwebtoken.ExpiredJwtException;
//import io.jsonwebtoken.MalformedJwtException;
//import io.jsonwebtoken.UnsupportedJwtException;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.http.HttpStatus;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import java.io.IOException;
//import java.util.List;
//
//
//@Component
//public class AuthTokenFilter extends OncePerRequestFilter {
//
//    Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);
//    private final JwtUtils jwtUtils;
//
//    public AuthTokenFilter(JwtUtils jwtUtils) {
//        this.jwtUtils = jwtUtils;
//    }
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//        logger.info("http request getting filtered");
//        try{
//            String jwt = request.getHeader("Authorization");
//            if (jwt != null && jwt.startsWith("Bearer ")) {
//                jwt = jwt.substring(7).trim(); // ✅ remove prefix and whitespace
//            }
//            logger.info("jwt found {}",jwt);
//            if (jwt != null && jwtUtils.validate(jwt)){
//
//                logger.info("jwt validated");
//                List<String> roles = jwtUtils.getUserRolesFromJwtToken(jwt);
//                logger.info("got user roles from jwt {}",roles);
//
//                List<SimpleGrantedAuthority> authorities = roles.stream().map(
//                        role -> new SimpleGrantedAuthority("ROLE_"+role)
//                        ).toList();
//
//                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
//                        null,
//                        null,
//                        authorities
//                );
//
//                SecurityContextHolder.getContext().setAuthentication(authentication);
//                logger.info("SecurityContextHolder updated with roles {}", authorities);
//            }
//        }catch (ExpiredJwtException e) {
//            logger.info(e.getMessage());
//            writeErrorResponse(response, new ErrorResponse("Token expired. Please login again.", HttpStatus.UNAUTHORIZED.value()), HttpStatus.UNAUTHORIZED);
//        } catch (MalformedJwtException | UnsupportedJwtException | IllegalArgumentException e) {
//            logger.info(e.getMessage());
//            writeErrorResponse(response, new ErrorResponse("Invalid token.", HttpStatus.UNAUTHORIZED.value()), HttpStatus.UNAUTHORIZED);
//        } catch (Exception e) {
//            logger.info(e.getMessage());
//            writeErrorResponse(response, new ErrorResponse("Authentication failed.", HttpStatus.INTERNAL_SERVER_ERROR.value()), HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//        filterChain.doFilter(request, response);
//
//    }
//
//
//    private void writeErrorResponse(HttpServletResponse response, ErrorResponse error, HttpStatus status) throws IOException {
//        response.setStatus(status.value());
//        response.setContentType("application/json");
//        response.getWriter().write(
//                String.format("{\"message\":\"%s\",\"status\":%d}", error.getMessage(), error.getStatus())
//        );
//    }
//
//}
