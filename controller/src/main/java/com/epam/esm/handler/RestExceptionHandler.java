package com.epam.esm.handler;

import com.epam.esm.dto.ErrorResponse;
import com.epam.esm.exception.*;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler{

    @ExceptionHandler ({BikeGoodsParametersException.class, CertificateParametersException.class,
                        IncorrectDataException.class, RegistrationException.class, ParameterException.class,
                        AuthenticationDataException.class, PriceException.class,
                        ThereIsNoSuchCertificateException.class, ThereIsNoSuchBikeGoodsException.class,
                        ThereIsNoSuchEntityException.class, ThereIsNoSuchOrderException.class,
                        ThereIsNoSuchUserException.class, EmptyResultDataAccessException.class,
                        FailedOperationException.class})
    public ResponseEntity<Object> handleEntityParametersException(Exception exception, WebRequest request) {
        ErrorResponse error = new ErrorResponse(exception.getLocalizedMessage(), exception.getClass().toString());
        return new ResponseEntity<>(error, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler (AccessDeniedException.class)
    public ResponseEntity<Object> handleAccessDeniedException(Exception exception, WebRequest request) {
        String message = "Access is denied. You are not authorized to access this resource.";
        ErrorResponse error = new ErrorResponse(message, exception.getClass().toString());
        return new ResponseEntity<>(error, new HttpHeaders(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler (DataIntegrityViolationException.class)
    public ResponseEntity<Object> handleDataIntegrityViolationException(Exception exception, WebRequest request) {
        String message = "Entity with such name already exists.";
        ErrorResponse error = new ErrorResponse(message, exception.getClass().toString());
        return new ResponseEntity<>(error, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler (JwtAuthenticationException.class)
    public ResponseEntity<Object> handleJwtAuthenticationException(Exception exception, WebRequest request) {
        ErrorResponse error = new ErrorResponse(exception.getMessage(), exception.getClass().toString());
        return new ResponseEntity<>(error, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<Object> handleAll(Exception exception, WebRequest request) {
        ErrorResponse error = new ErrorResponse(exception.getLocalizedMessage(), exception.getClass().toString());
        return new ResponseEntity<>(error, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
