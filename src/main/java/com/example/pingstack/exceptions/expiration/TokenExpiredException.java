package com.example.pingstack.exceptions.expiration;

public class TokenExpiredException extends RuntimeException{
    public TokenExpiredException (String message ) {
        super(message);
    }
}
