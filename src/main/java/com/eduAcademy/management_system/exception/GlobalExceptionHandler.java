package com.eduAcademy.management_system.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private ResponseEntity<ApiError> buildResponseEntity(HttpStatus status, String error, String message, WebRequest request) {
        ApiError apiError = new ApiError(
                status.value(),
                message,
                request.getDescription(false)
        );
        return new ResponseEntity<>(apiError, status);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ApiError> handleUnauthorizedException(Exception ex, WebRequest request) {
        return buildResponseEntity(HttpStatus.UNAUTHORIZED, "Accès Non Autorisé", ex.getMessage(), request);
    }
}
