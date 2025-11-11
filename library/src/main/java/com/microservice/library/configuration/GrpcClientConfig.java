package com.microservice.library.configuration;

import com.microservice.grpc.book.BookServiceGrpc;
import com.microservice.grpc.user.UserServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GrpcClientConfig {

    @Bean(destroyMethod = "shutdownNow")
    public ManagedChannel bookChannel() {
        return ManagedChannelBuilder.forAddress("localhost", 9092)
                .usePlaintext()
                .build();
    }

    @Bean(destroyMethod = "shutdownNow")
    public ManagedChannel userChannel() {
        return ManagedChannelBuilder.forAddress("localhost", 9091)
                .usePlaintext()
                .build();
    }

    @Bean
    public BookServiceGrpc.BookServiceBlockingStub bookStub(@Qualifier("bookChannel") ManagedChannel bookChannel) {
        return BookServiceGrpc.newBlockingStub(bookChannel);
    }

    @Bean
    public UserServiceGrpc.UserServiceBlockingStub userStub(@Qualifier("userChannel") ManagedChannel userChannel) {
        return UserServiceGrpc.newBlockingStub(userChannel);
    }


}
