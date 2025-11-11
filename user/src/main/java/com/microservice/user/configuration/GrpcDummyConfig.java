package com.microservice.user.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GrpcDummyConfig {

    @Bean
    public net.devh.boot.grpc.server.security.authentication.GrpcAuthenticationReader grpcAuthenticationReader() {
        return (call, headers) -> null;
    }
}
