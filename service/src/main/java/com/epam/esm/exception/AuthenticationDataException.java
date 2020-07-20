package com.epam.esm.exception;

public class AuthenticationDataException extends RuntimeException{
    public AuthenticationDataException() {}

    public AuthenticationDataException(String message, Throwable cause) {
        super(message, cause);
    }

    public AuthenticationDataException(String message) {
        super(message);
    }

    public AuthenticationDataException(Throwable e) {
        super(e);
    }
}
