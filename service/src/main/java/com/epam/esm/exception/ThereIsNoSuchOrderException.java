package com.epam.esm.exception;

public class ThereIsNoSuchOrderException extends RuntimeException{

    public ThereIsNoSuchOrderException() {}

    public ThereIsNoSuchOrderException(String message, Throwable cause) {
        super(message, cause);
    }

    public ThereIsNoSuchOrderException(String message) {
        super(message);
    }

    public ThereIsNoSuchOrderException(Throwable e) {
        super(e);
    }
}
