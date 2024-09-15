package com.encora.samples.nimbus.security.auth.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * Service to validate password complexity rules.
 */
@Service
@Slf4j
public class PasswordValidateService {

  /**
   * Validates if the password meets the complexity requirements.
   *
   * @param password The password to validate.
   * @return A Mono emitting true if the password is valid, false otherwise.
   */
  public Mono<Boolean> validatePassword(String password) {

    return Mono.create(sink -> {
      boolean isValid = true;

      if (password.length() < 8) {
        log.warn("{}" ,"La contraseña debe tener al menos 8 caracteres.");
        isValid = false;
      }

      if (!password.matches(".*[A-Z].*")) {
        log.warn("La contraseña debe contener al menos una letra mayúscula.");
        isValid = false;
      }

      if (!password.matches(".*\\d.*")) {
        log.warn("La contraseña debe contener al menos un número.");
        isValid = false;
      }

      if (!password.matches(".*[!@#\\$%\\^&\\*].*")) {
        log.warn("La contraseña debe contener al menos un carácter especial (por ejemplo, ! @ # $ % ^ & *).");
        isValid = false;
      }

      sink.success(isValid);
    });
  }


}

