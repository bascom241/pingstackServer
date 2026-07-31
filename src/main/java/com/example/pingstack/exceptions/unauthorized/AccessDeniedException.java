package com.example.pingstack.exceptions.unauthorized;

public class AccessDeniedException extends RuntimeException{
    public AccessDeniedException(String message){
        super(message);
    }
}
