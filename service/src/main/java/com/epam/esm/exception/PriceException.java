package com.epam.esm.exception;

public class PriceException extends RuntimeException{

    public PriceException() {}

    public PriceException(String message, Throwable cause) {
        super(message, cause);
    }

    public PriceException(String message) {
        super(message);
    }

    public PriceException(Throwable e) {
        super(e);
    }
}
