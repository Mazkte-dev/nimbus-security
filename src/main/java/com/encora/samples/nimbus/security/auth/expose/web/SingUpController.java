package com.encora.samples.nimbus.security.auth.expose.web;

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
import reactor.core.publisher.Mono;

/**
 * Controller to handle user registration requests.
 */
@RestController
@RequestMapping("${application.api.path}/security/users")
@RequiredArgsConstructor
public class SingUpController {

  private final AuthenticationService authenticationService;

  /**
   * Registers a new user.
   *
   * @param signUpRequest The request body containing the user's sign-up information.
   * @return A Mono emitting a ResponseEntity with the created user information if successful.
   */
  @PostMapping("/signup")
  public Mono<ResponseEntity<ServiceResponse>> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
    return authenticationService.registerUser(signUpRequest)
            .map(createdUser -> ResponseEntity.status(HttpStatus.CREATED)
                    .body(ServiceResponse.success(createdUser))
            );
  }

}