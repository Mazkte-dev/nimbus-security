package com.encora.sample.nimbus.security.auth.expose.web;

import com.encora.samples.nimbus.security.auth.expose.web.AuthenticationController;
import com.encora.samples.nimbus.security.auth.model.api.AuthenticationRequest;
import com.encora.samples.nimbus.security.auth.model.api.AuthenticationResponse;
import com.encora.samples.nimbus.security.auth.model.api.ServiceResponse;
import com.encora.samples.nimbus.security.auth.service.AuthenticationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthenticationControllerTest {

    @Mock
    private AuthenticationService authenticationService;

    @InjectMocks
    private AuthenticationController authenticationController;

    @Test
    void testAuthenticateUser() {
        AuthenticationRequest authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setEmail("test@example.com");
        authenticationRequest.setPassword("password");

        AuthenticationResponse authenticationResponse = new AuthenticationResponse();
        authenticationResponse.setToken("jwtToken");

        when(authenticationService.authenticateUser(authenticationRequest)).thenReturn(Mono.just(authenticationResponse));

        Mono<ResponseEntity<ServiceResponse>> result = authenticationController.authenticateUser(authenticationRequest);

        StepVerifier.create(result)
                .assertNext(response -> {
                    assertEquals(HttpStatus.OK, response.getStatusCode());
                    assertEquals(authenticationResponse, response.getBody().getData());
                })
                .verifyComplete();
    }

    @Test
    void testLogout() {
        ServerWebExchange exchange = MockServerWebExchange.builder(MockServerHttpRequest.get("/logout")
                        .build())
                .build();

        Mono<ResponseEntity<Void>> result = authenticationController.logout(exchange);

        StepVerifier.create(result)
                .assertNext(response -> {
                    assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
                    assertEquals("Bearer invalid", exchange.getResponse()
                            .getHeaders()
                            .getFirst("Authorization"));
                })
                .verifyComplete();
    }
}

