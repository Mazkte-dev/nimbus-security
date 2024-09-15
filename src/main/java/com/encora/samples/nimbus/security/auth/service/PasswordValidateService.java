package com.encora.samples.nimbus.security.auth.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class PasswordValidateService {

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

      if (!password.matches(".*[0-9].*")) {
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
