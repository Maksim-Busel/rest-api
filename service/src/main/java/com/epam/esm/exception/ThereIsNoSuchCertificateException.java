package com.epam.esm.exception;

public class ThereIsNoSuchCertificateException extends RuntimeException{

    public ThereIsNoSuchCertificateException() {}

    public ThereIsNoSuchCertificateException(String message, Throwable cause) {
        super(message, cause);
    }

    public ThereIsNoSuchCertificateException(String message) {
        super(message);
    }

    public ThereIsNoSuchCertificateException(Throwable e) {
        super(e);
    }
}
