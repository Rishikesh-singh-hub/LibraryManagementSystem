package com.microservice.user.service;

import com.microservice.grpc.user.IssueUserRequest;
import com.microservice.grpc.user.UserRequest;
import com.microservice.grpc.user.UserResponse;
import com.microservice.grpc.user.UserServiceGrpc;
import com.microservice.user.dto.UserMapper;
import com.microservice.user.entity.Role;
import com.microservice.user.entity.UserEntity;
import com.microservice.user.repository.UserRepo;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;


@GrpcService
public class UserGrpcService extends UserServiceGrpc.UserServiceImplBase {

    Logger logger = LoggerFactory.getLogger(UserGrpcService.class);
    private final UserRepo userRepo;
    private final UserMapper userMapper;

    public UserGrpcService(UserRepo userRepo, UserMapper userMapper) {
        this.userRepo = userRepo;
        this.userMapper = userMapper;
    }


    @Override
    public void getUserById(UserRequest request, StreamObserver<UserResponse> responseObserver) {
        logger.info("Got request for id = {}",request.getId());

        UserEntity entity = userRepo.findById(request.getId()).orElseThrow(()-> new UsernameNotFoundException("no user found"));
        UserResponse response = userMapper.getUserResponseById(request.getId());

        logger.info("sending User details ");
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }



    @Override
    public void issueBooksUpdate(IssueUserRequest userReq, StreamObserver<UserResponse> responseObserver){

        logger.info("updating user after issuing book");

        UserEntity entity = userRepo.findById(userReq.getId()).orElseThrow();

        List<Role> roles = entity.getRoles();

        boolean isSuspend = roles.stream().anyMatch(Role.SUSPENDED::equals);

        int a;
        if(userReq.getIssue())
            a=userReq.getCount();
        else {
            a = -(userReq.getCount());
            roles.remove(Role.SUSPENDED);
            entity.setRoles(roles);
        }

        entity.setBooksIssuedCount(a+entity.getBooksIssuedCount());
        userRepo.save(entity);
        UserResponse userResponse = userMapper.getUserResponseById(entity.getId());


        responseObserver.onNext(userResponse);
        responseObserver.onCompleted();

    }

}
