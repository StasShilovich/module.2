package com.epam.esm.controller.exception;

import com.epam.esm.model.service.exception.NotExistEntityException;
import com.epam.esm.model.service.exception.ServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.concurrent.atomic.AtomicInteger;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestControllerAdvice
public class RestExceptionHandler {

    private final AtomicInteger atomicInteger = new AtomicInteger();

    @ExceptionHandler(ServiceException.class)
    private ResponseEntity<ErrorResponse> handleException(ServiceException exception) {
        return ResponseEntity.status(BAD_REQUEST).body(buildErrorResponse(BAD_REQUEST, exception.getLocalizedMessage()));
    }

    @ExceptionHandler(NotExistEntityException.class)
    private ResponseEntity<ErrorResponse> handleNotExistException(NotExistEntityException exception) {
        return ResponseEntity.status(NOT_FOUND).body(buildErrorResponse(NOT_FOUND, exception.getLocalizedMessage()));
    }

    @ExceptionHandler(RuntimeException.class)
    private ResponseEntity<ErrorResponse> handleNotExistException(RuntimeException exception) {
        String message = exception.getClass().getName() + " : " + exception.getMessage();
        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(buildErrorResponse(INTERNAL_SERVER_ERROR, message));
    }

    private ErrorResponse buildErrorResponse(HttpStatus status, String message) {
        return new ErrorResponse(message, status.value() * 100 + atomicInteger.incrementAndGet());
    }
}
