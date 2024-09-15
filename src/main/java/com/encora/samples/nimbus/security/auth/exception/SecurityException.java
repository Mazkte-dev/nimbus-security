package com.encora.samples.nimbus.security.auth.exception;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

@Data
@Slf4j
public class SecurityException extends Throwable {

  private HttpStatus status;

  public SecurityException(HttpStatus status, String message) {
    super(message);
    this.status = status;
  }

  public SecurityException(HttpStatus status, String message, Throwable cause) {
    super(message , cause);
    log.error(cause.getMessage());
    this.status = status;
  }

  public SecurityException(String message) {
    super(message);
  }

}
