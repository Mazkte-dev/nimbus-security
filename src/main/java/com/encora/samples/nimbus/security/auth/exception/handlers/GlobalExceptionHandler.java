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

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(SecurityException.class)
    public ResponseEntity<ServiceResponse> handleSecurityException(SecurityException ex) {
        ErrorResponse errorResponse = new ErrorResponse(ex.getStatus().value(), ex.getMessage());
        return ResponseEntity.badRequest().body(ServiceResponse.failed(errorResponse));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ServiceResponse> handleRuntimeException(RuntimeException ex) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
        return ResponseEntity.badRequest().body(ServiceResponse.failed(errorResponse));
    }

    @ExceptionHandler(ServerWebInputException.class)
    public ResponseEntity<ServiceResponse> handleServerWebInputException(ServerWebInputException ex) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), ex.getReason());
        return ResponseEntity.badRequest().body(ServiceResponse.failed(errorResponse));
    }

    @ExceptionHandler(WebExchangeBindException.class)
    public ResponseEntity<ServiceResponse> handleWebExchangeBindException(WebExchangeBindException ex) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
        return ResponseEntity.badRequest().body(ServiceResponse.failed(errorResponse));
    }
}