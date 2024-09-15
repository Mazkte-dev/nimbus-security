package com.encora.sample.nimbus.security.auth.service;

import com.encora.samples.nimbus.security.auth.service.PasswordValidateService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class PasswordValidateServiceTest {

  @InjectMocks
  private PasswordValidateService passwordValidateService;

  @Test
  void testValidatePasswordValid() {
    String password = "Password123!";

    Mono<Boolean> result = passwordValidateService.validatePassword(password);

    StepVerifier.create(result)
            .assertNext(isValid -> {
              assertTrue(isValid);
            })
            .verifyComplete();
  }

  @Test
  void testValidatePasswordTooShort() {
    String password = "Pass1!";

    Mono<Boolean> result = passwordValidateService.validatePassword(password);

    StepVerifier.create(result)
            .assertNext(isValid -> {
              assertFalse(isValid);
            })
            .verifyComplete();
  }

  @Test
  void testValidatePasswordNoUppercase() {
    String password = "password123!";

    Mono<Boolean> result = passwordValidateService.validatePassword(password);

    StepVerifier.create(result)
            .assertNext(isValid -> {
              assertFalse(isValid);
            })
            .verifyComplete();
  }

  @Test
  void testValidatePasswordNoNumber() {
    String password = "Password!";

    Mono<Boolean> result = passwordValidateService.validatePassword(password);

    StepVerifier.create(result)
            .assertNext(isValid -> {
              assertFalse(isValid);
            })
            .verifyComplete();
  }

  @Test
  void testValidatePasswordNoSpecialCharacter() {
    String password = "Password123";

    Mono<Boolean> result = passwordValidateService.validatePassword(password);

    StepVerifier.create(result)
            .assertNext(isValid -> {
              assertFalse(isValid);
            })
            .verifyComplete();
  }
}

