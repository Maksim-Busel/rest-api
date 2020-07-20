package com.epam.esm.exception;

public class FailedOperationException extends RuntimeException{

    public FailedOperationException() {}

    public FailedOperationException(String message, Throwable cause) {
        super(message, cause);
    }

    public FailedOperationException(String message) {
        super(message);
    }

    public FailedOperationException(Throwable e) {
        super(e);
    }
}
