package com.encora.samples.nimbus.apigateway.filters;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.http.server.reactive.MockServerHttpResponse;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class JwtAuthenticationFilterTest {

    private JwtAuthenticationFilter filter;
    private String jwtSecret;

    @BeforeEach
    void setUp() {
        jwtSecret = "CKqHITrMPzepvWmczHw+O4QpWgYKlX0b3hqrBxYWu2UWN9Xrr2zKq8ziOqMOfYiLSP5pJYR2FOoJpRGLUrGVeQ==";
        filter = new JwtAuthenticationFilter();
        ReflectionTestUtils.setField(filter, "jwtSecret", jwtSecret);
    }

    @Test
    void testApplyWhenAuthorizationHeaderIsMissing() {
        ServerWebExchange exchange = MockServerWebExchange.from(MockServerHttpRequest.get("/test").build());
        GatewayFilterChain chain = Mockito.mock(GatewayFilterChain.class);
        when(chain.filter(any())).thenReturn(Mono.empty());

        Mono<Void> result = filter.apply(new JwtAuthenticationFilter.Config()).filter(exchange, chain);

        StepVerifier.create(result)
                .expectComplete()
                .verify();

        assertEquals(HttpStatus.UNAUTHORIZED, exchange.getResponse().getStatusCode());
    }

    @Test
    void testApplyWhenJwtTokenIsInvalid() {
        String invalidJwt = "invalid-jwt";
        ServerWebExchange exchange = MockServerWebExchange.from(MockServerHttpRequest.get("/test")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + invalidJwt)
                .build());
        GatewayFilterChain chain = Mockito.mock(GatewayFilterChain.class);
        when(chain.filter(any())).thenReturn(Mono.empty());

        Mono<Void> result = filter.apply(new JwtAuthenticationFilter.Config()).filter(exchange, chain);

        StepVerifier.create(result)
                .expectComplete()
                .verify();

        assertEquals(HttpStatus.UNAUTHORIZED, exchange.getResponse().getStatusCode());
    }

    @Test
    void testApplyWhenJwtTokenIsValid() {
        String userId = "test-user";
        String jwt = generateJwtToken(userId, jwtSecret);
        ServerWebExchange exchange = MockServerWebExchange.from(MockServerHttpRequest.get("/test")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)
                .build());
        GatewayFilterChain chain = Mockito.mock(GatewayFilterChain.class);
        when(chain.filter(any())).thenReturn(Mono.empty());

        Mono<Void> result = filter.apply(new JwtAuthenticationFilter.Config()).filter(exchange, chain);

        StepVerifier.create(result)
                .expectComplete()
                .verify();

        ServerHttpRequest modifiedRequest = exchange.getRequest();
        assertEquals(userId, modifiedRequest.getHeaders().getFirst("X-User-Id"));
        assertNotNull(modifiedRequest.getHeaders().getFirst("X-Request-Id"));
        assertNotNull(modifiedRequest.getHeaders().getFirst("X-Request-Date"));
    }

    private String generateJwtToken(String userId, String secret) {
        return Jwts.builder()
                .setSubject(userId)
                .signWith(io.jsonwebtoken.SignatureAlgorithm.HS512, secret)
                .compact();
    }
}
