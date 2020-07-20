package com.epam.esm.exception;

public class IncorrectDataException extends RuntimeException {

    public IncorrectDataException() {}

    public IncorrectDataException(String message, Throwable cause) {
        super(message, cause);
    }

    public IncorrectDataException(String message) {
        super(message);
    }

    public IncorrectDataException(Throwable e) {
        super(e);
    }
}
