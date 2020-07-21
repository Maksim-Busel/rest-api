package com.epam.esm.handler;

import com.epam.esm.dto.ErrorResponse;
import com.epam.esm.exception.*;
import com.epam.esm.exception.JwtAuthenticationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler (DataIntegrityViolationException.class)
    public ResponseEntity<Object> handleDataIntegrityViolationException(Exception exception, WebRequest request) {
        String message = "Entity with such name already exists.";
        ErrorResponse error = new ErrorResponse(message, exception.getClass().toString());
        return new ResponseEntity<>(error, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler ({BikeGoodsParametersException.class, CertificateParametersException.class,
                        IncorrectDataException.class, RegistrationException.class, ParameterException.class})
    public ResponseEntity<Object> handleEntityParametersException(Exception exception, WebRequest request) {
        ErrorResponse error = new ErrorResponse(exception.getLocalizedMessage(), exception.getClass().toString());
        return new ResponseEntity<>(error, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({JwtAuthenticationException.class})
    public ResponseEntity<Object> handleJwtAuthenticationException(Exception exception, WebRequest request) {
        ErrorResponse error = new ErrorResponse(exception.getLocalizedMessage(), exception.getClass().toString());
        return new ResponseEntity<>(error, new HttpHeaders(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler({AuthenticationDataException.class})
    public ResponseEntity<Object> handleAuthenticationDataException(Exception exception, WebRequest request) {
        ErrorResponse error = new ErrorResponse(exception.getLocalizedMessage(), exception.getClass().toString());
        return new ResponseEntity<>(error, new HttpHeaders(), HttpStatus.NON_AUTHORITATIVE_INFORMATION);
    }

    @ExceptionHandler({PriceException.class})
    public ResponseEntity<Object> handlePriceException(Exception exception, WebRequest request) {
        ErrorResponse error = new ErrorResponse(exception.getLocalizedMessage(), exception.getClass().toString());
        return new ResponseEntity<>(error, new HttpHeaders(), HttpStatus.REQUESTED_RANGE_NOT_SATISFIABLE);
    }

    @ExceptionHandler ({FailedOperationException.class})
    public ResponseEntity<Object> handleFailedOperationException(Exception exception, WebRequest request) {
        ErrorResponse error = new ErrorResponse(exception.getLocalizedMessage(), exception.getClass().toString());
        return new ResponseEntity<>(error, new HttpHeaders(), HttpStatus.EXPECTATION_FAILED);
    }

    @ExceptionHandler ({ThereIsNoSuchCertificateException.class, ThereIsNoSuchBikeGoodsException.class,
                        ThereIsNoSuchEntityException.class, ThereIsNoSuchOrderException.class,
                        ThereIsNoSuchUserException.class, EmptyResultDataAccessException.class})
    public ResponseEntity<Object> handleNoSuchEntityException(Exception exception, WebRequest request) {
        ErrorResponse error = new ErrorResponse(exception.getLocalizedMessage(), exception.getClass().toString());
        return new ResponseEntity<>(error, new HttpHeaders(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<Object> handleAll(Exception exception, WebRequest request) {
        ErrorResponse error = new ErrorResponse(exception.getLocalizedMessage(), exception.getClass().toString());
        return new ResponseEntity<>(error, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
