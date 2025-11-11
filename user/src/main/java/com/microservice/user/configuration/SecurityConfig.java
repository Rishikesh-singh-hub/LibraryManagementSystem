package com.microservice.user.configuration;

import com.microservice.user.jwt.AuthTokenFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

    Logger logger =  LoggerFactory.getLogger(SecurityConfig.class);
    private final AuthTokenFilter authTokenFilter;
    private final UserDetailsService userDetailsService;

    public SecurityConfig(AuthTokenFilter authTokenFilter, UserDetailsService userDetailsService) {
        this.authTokenFilter = authTokenFilter;
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        logger.info("Securing");
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(
                req -> req
                        .requestMatchers("/api/**").permitAll()
                        .requestMatchers("/auth/**").authenticated()
        )
        .addFilterBefore(authTokenFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();

    }


    public void authenticationManager(AuthenticationManagerBuilder auth)throws Exception{
        auth.userDetailsService(userDetailsService).passwordEncoder(BeanCreater.passwordEncoder());
    }



}
