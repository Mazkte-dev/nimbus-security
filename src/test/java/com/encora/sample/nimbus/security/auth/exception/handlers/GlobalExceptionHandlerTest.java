package com.encora.sample.nimbus.security.auth.exception.handlers;

import com.encora.samples.nimbus.security.auth.exception.SecurityException;
import com.encora.samples.nimbus.security.auth.exception.handlers.GlobalExceptionHandler;
import com.encora.samples.nimbus.security.auth.model.api.ErrorResponse;
import com.encora.samples.nimbus.security.auth.model.api.ServiceResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ServerWebInputException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler exceptionHandler = new GlobalExceptionHandler();

    @Test
    void testHandleSecurityException() {
        SecurityException ex = new SecurityException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        ResponseEntity<ServiceResponse<ErrorResponse>> response = exceptionHandler.handleSecurityException(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(HttpStatus.UNAUTHORIZED.value(), response.getBody().getError().getStatus());
        assertEquals("Unauthorized", response.getBody().getError().getMessage());
    }

    @Test
    void testHandleRuntimeException() {
        RuntimeException ex = new RuntimeException("Internal Server Error");
        ResponseEntity<ServiceResponse<ErrorResponse>> response = exceptionHandler.handleRuntimeException(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getBody().getError().getStatus());
        assertEquals("Internal Server Error", response.getBody().getError().getMessage());
    }

    @Test
    void testHandleServerWebInputException() {
        ServerWebInputException ex = new ServerWebInputException("Bad Request");
        ResponseEntity<ServiceResponse<ErrorResponse>> response = exceptionHandler.handleServerWebInputException(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getBody().getError().getStatus());
        assertEquals("Bad Request", response.getBody().getError().getMessage());
    }

}

