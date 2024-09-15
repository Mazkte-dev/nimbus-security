package com.encora.samples.nimbus.security.auth.exception.handlers;

import com.encora.samples.nimbus.security.auth.exception.SecurityException;
import com.encora.samples.nimbus.security.auth.model.api.ErrorResponse;
import com.encora.samples.nimbus.security.auth.model.api.ServiceResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.ServerWebInputException;

/**
 * Global exception handler for handling exceptions and returning appropriate error responses.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles SecurityException.
     *
     * @param ex The SecurityException to handle.
     * @return A ResponseEntity containing an error response with the exception details.
     */
    @ExceptionHandler(SecurityException.class)
    public ResponseEntity<ServiceResponse<ErrorResponse>> handleSecurityException(SecurityException ex) {
        ErrorResponse errorResponse = new ErrorResponse(ex.getStatus().value(), ex.getMessage());
        return ResponseEntity.badRequest().body(ServiceResponse.failed(errorResponse));
    }

    /**
     * Handles RuntimeException.
     *
     * @param ex The RuntimeException to handle.
     * @return A ResponseEntity containing an error response with the exception details.
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ServiceResponse<ErrorResponse>> handleRuntimeException(RuntimeException ex) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
        return ResponseEntity.badRequest().body(ServiceResponse.failed(errorResponse));
    }

    /**
     * Handles ServerWebInputException.
     *
     * @param ex The ServerWebInputException to handle.
     * @return A ResponseEntity containing an error response with the exception details.
     */
    @ExceptionHandler(ServerWebInputException.class)
    public ResponseEntity<ServiceResponse<ErrorResponse>> handleServerWebInputException(ServerWebInputException ex) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), ex.getReason());
        return ResponseEntity.badRequest().body(ServiceResponse.failed(errorResponse));
    }

    /**
     * Handles WebExchangeBindException.
     *
     * @param ex The WebExchangeBindException to handle.
     * @return A ResponseEntity containing an error response with the exception details.
     */
    @ExceptionHandler(WebExchangeBindException.class)
    public ResponseEntity<ServiceResponse<ErrorResponse>> handleWebExchangeBindException(WebExchangeBindException ex) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
        return ResponseEntity.badRequest().body(ServiceResponse.failed(errorResponse));
    }
}

