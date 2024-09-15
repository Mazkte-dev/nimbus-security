package com.encora.sample.nimbus.security.auth.expose.web;

import com.encora.samples.nimbus.security.auth.expose.web.SingUpController;
import com.encora.samples.nimbus.security.auth.model.api.ServiceResponse;
import com.encora.samples.nimbus.security.auth.model.api.SignUpRequest;
import com.encora.samples.nimbus.security.auth.model.api.SignUpResponse;
import com.encora.samples.nimbus.security.auth.service.AuthenticationService;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SingUpControllerTest {

  @Mock
  private AuthenticationService authenticationService;

  @InjectMocks
  private SingUpController singUpController;

  @Test
  void testRegisterUser() {
    SignUpRequest signUpRequest = new SignUpRequest();
    signUpRequest.setEmail("test@example.com");
    signUpRequest.setPassword("password");

    SignUpResponse signUpResponse = new SignUpResponse();
    signUpResponse.setId("1233000");
    signUpResponse.setCreatedDate(LocalDateTime.now());

    when(authenticationService.registerUser(any(SignUpRequest.class))).thenReturn(Mono.just(signUpResponse));

    Mono<ResponseEntity<ServiceResponse>> result = singUpController.registerUser(signUpRequest);

    StepVerifier.create(result)
            .assertNext(response -> {
              assertEquals(HttpStatus.CREATED, response.getStatusCode());
              assertEquals(signUpResponse.getId(),
                      ((SignUpResponse)response.getBody().getData()).getId());
            })
            .verifyComplete();
  }
}

