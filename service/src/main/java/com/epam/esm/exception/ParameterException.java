package com.epam.esm.exception;

public class ParameterException extends RuntimeException{

    public ParameterException() {}

    public ParameterException(String message, Throwable cause) {
        super(message, cause);
    }

    public ParameterException(String message) {
        super(message);
    }

    public ParameterException(Throwable e) {
        super(e);
    }
}
