package com.epam.esm.exception;

public class RegistrationException extends RuntimeException{
    public RegistrationException() {}

    public RegistrationException(String message, Throwable cause) {
        super(message, cause);
    }

    public RegistrationException(String message) {
        super(message);
    }

    public RegistrationException(Throwable e) {
        super(e);
    }
}
