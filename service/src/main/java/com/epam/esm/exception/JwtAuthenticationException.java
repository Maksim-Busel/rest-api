package com.epam.esm.exception;

public class JwtAuthenticationException extends RuntimeException{

    public JwtAuthenticationException() {}

    public JwtAuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }

    public JwtAuthenticationException(String message) {
        super(message);
    }

    public JwtAuthenticationException(Throwable e) {
        super(e);
    }
}
