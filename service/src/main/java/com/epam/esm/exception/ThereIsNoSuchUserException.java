package com.epam.esm.exception;

public class ThereIsNoSuchUserException extends RuntimeException{

    public ThereIsNoSuchUserException() {}

    public ThereIsNoSuchUserException(String message, Throwable cause) {
        super(message, cause);
    }

    public ThereIsNoSuchUserException(String message) {
        super(message);
    }

    public ThereIsNoSuchUserException(Throwable e) {
        super(e);
    }
}
