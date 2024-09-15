package com.encora.samples.nimbus.security.auth.expose.web;


import com.encora.samples.nimbus.security.auth.model.api.AuthenticationRequest;
import com.encora.samples.nimbus.security.auth.model.api.ServiceResponse;
import com.encora.samples.nimbus.security.auth.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;


/**
 * Controller to handle authentication requests.
 */
@RestController
@RequestMapping("${application.api.path}/security/auth/")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    /**
     * Authenticates a user with the provided credentials.
     *
     * @param authenticationRequest The authentication request containing the user's email and password.
     * @return A Mono emitting a ResponseEntity with a ServiceResponse containing the authentication token if successful.
     */
    @PostMapping("login")
    public Mono<ResponseEntity<ServiceResponse>> authenticateUser(@Valid @RequestBody AuthenticationRequest authenticationRequest) {
        return authenticationService.authenticateUser(authenticationRequest)
                .map(authenticationResponse -> ResponseEntity.ok(ServiceResponse
                        .success(authenticationResponse))
                );
    }

    /**
     * Logs out the current user.
     *
     * @param exchange The ServerWebExchange object.
     * @return A Mono emitting a ResponseEntity with no content.
     */
    @PostMapping("logout")
    public Mono<ResponseEntity<Void>> logout(ServerWebExchange exchange) {
        // 1. Optionally Invalidate JWT (Client-Side)a
        //    -  You can set a response header to instruct the client to remove the JWT.
        exchange.getResponse().getHeaders().set("Authorization", "Bearer invalid");
        // 2. Return Success Response
        return Mono.just(ResponseEntity.noContent().build());
    }

}

