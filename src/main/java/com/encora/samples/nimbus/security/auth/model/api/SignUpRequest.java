package com.encora.samples.nimbus.security.auth.model.api;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class SignUpRequest {

  @NotBlank(message = "Email is required")
  @Pattern(regexp = "^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$",
          message = "Email is invalid")
  private String email;

  @NotBlank(message = "Password is required")
  @Pattern(regexp = "^(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#\\$%\\^&\\*])(?=.{8,}).*$",
          message = "This password does not meet the length, complexity, age, or history requirements of your corporate password policy.")
  private String password;
}
