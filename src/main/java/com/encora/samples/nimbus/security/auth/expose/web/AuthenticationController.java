package com.encora.samples.nimbus.security.auth.expose.web;


import com.encora.samples.nimbus.security.auth.model.api.AuthenticationRequest;
import com.encora.samples.nimbus.security.auth.model.api.ServiceResponse;
import com.encora.samples.nimbus.security.auth.model.api.SignUpRequest;
import com.encora.samples.nimbus.security.auth.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;


@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/signup")
    public Mono<ResponseEntity<ServiceResponse>> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
        return authenticationService.registerUser(signUpRequest)
                .map(createdUser -> ResponseEntity.status(HttpStatus.CREATED)
                        .body(ServiceResponse.success(createdUser))
                );
    }

    @PostMapping("/login")
    public Mono<ResponseEntity<ServiceResponse>> authenticateUser(@Valid @RequestBody AuthenticationRequest authenticationRequest) {
        return authenticationService.authenticateUser(authenticationRequest)
                .map(authenticationResponse -> ResponseEntity.ok(ServiceResponse
                        .success(authenticationResponse))
                );
    }

    @PostMapping("/logout")
    public Mono<ResponseEntity<Void>> logout(ServerWebExchange exchange) {
        // 1. Optionally Invalidate JWT (Client-Side)
        //    -  You can set a response header to instruct the client to remove the JWT.
        exchange.getResponse().getHeaders().set("Authorization", "Bearer invalid");
        // 2. Return Success Response
        return Mono.just(ResponseEntity.noContent().build());
    }

}