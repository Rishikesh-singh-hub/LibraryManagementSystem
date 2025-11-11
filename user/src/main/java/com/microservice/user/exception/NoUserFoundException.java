package com.microservice.user.exception;

public class NoUserFoundException extends Exception{

    public NoUserFoundException(String message){
        super(message);
    }

}
