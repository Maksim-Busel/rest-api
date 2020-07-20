package com.epam.esm.exception;

public class ThereIsNoSuchEntityException extends RuntimeException{
    public ThereIsNoSuchEntityException() {}

    public ThereIsNoSuchEntityException(String message, Throwable cause) {
        super(message, cause);
    }

    public ThereIsNoSuchEntityException(String message) {
        super(message);
    }

    public ThereIsNoSuchEntityException(Throwable e) {
        super(e);
    }
}
