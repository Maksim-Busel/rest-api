package com.epam.esm.exception;

public class CertificateParametersException extends RuntimeException{

    public CertificateParametersException() {}

    public CertificateParametersException(String message, Throwable cause) {
        super(message, cause);
    }

    public CertificateParametersException(String message) {
        super(message);
    }

    public CertificateParametersException(Throwable e) {
        super(e);
    }
}
