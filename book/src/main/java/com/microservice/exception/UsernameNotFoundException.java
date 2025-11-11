package com.microservice.exception;

public class UsernameNotFoundException extends Exception{
    public UsernameNotFoundException(String str){
        super(str);
    }
}
