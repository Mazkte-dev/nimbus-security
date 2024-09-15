package com.encora.samples.nimbus.security.auth.service;


import com.encora.samples.nimbus.security.auth.exception.SecurityException;
import com.encora.samples.nimbus.security.auth.mapper.UserMapper;
import com.encora.samples.nimbus.security.auth.model.api.AuthenticationRequest;
import com.encora.samples.nimbus.security.auth.model.api.AuthenticationResponse;
import com.encora.samples.nimbus.security.auth.model.api.SignUpRequest;
import com.encora.samples.nimbus.security.auth.model.api.SignUpResponse;
import com.encora.samples.nimbus.security.auth.model.domain.User;
import com.encora.samples.nimbus.security.auth.repository.UserRepository;
import com.encora.samples.nimbus.security.auth.utils.JwtUtils;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * Service to handle authentication and registration requests.
 */
@Service
@RequiredArgsConstructor
public class AuthenticationService {

  private final UserRepository userRepository;

  private final PasswordEncoder passwordEncoder;

  private final JwtUtils jwtUtils;

  private final UserMapper userMapper;

  private final PasswordValidateService passwordValidateService;

  /**
   * Authenticates a user with the provided credentials.
   *
   * @param authenticationRequest The authentication request containing the user's email and password.
   * @return A Mono emitting an AuthenticationResponse with a JWT token if the authentication is successful.
   */
  public Mono<AuthenticationResponse> authenticateUser(AuthenticationRequest authenticationRequest) {
    return userRepository.findByEmail(authenticationRequest.getEmail())
            .filter(user -> passwordEncoder.matches(authenticationRequest.getPassword(),
                    user.getPasswordHash()))
            .map(user -> AuthenticationResponse.builder()
                    .token(jwtUtils.generateJwtToken(user.getId()))
                    .build()
            )
            .switchIfEmpty(Mono.error(new SecurityException("Invalid username or password")))
            .onErrorResume(throwable -> {
              if (throwable instanceof SecurityException) {
                return Mono.error(throwable);
              }
              return Mono.error(new SecurityException(HttpStatus.INTERNAL_SERVER_ERROR,
                      "Error authentication user", throwable));
            });
  }

  /**
   * Registers a new user with the provided sign-up information.
   *
   * @param signUpRequest The sign-up request containing the user's email, password.
   * @return  A Mono emitting a SignUpResponse if the registration is successful.
   */
  public Mono<SignUpResponse> registerUser(SignUpRequest signUpRequest) {
    return userRepository.findByEmail(signUpRequest.getEmail())
            .switchIfEmpty(Mono.just( new User()))
            .flatMap(existingUser -> {
              if (StringUtils.isBlank(existingUser.getId())) {

                return passwordValidateService.validatePassword(signUpRequest.getPassword())
                        .flatMap(valid -> {
                          if (valid) {
                            User user = userMapper.toUser(signUpRequest);
                            user.setPasswordHash(passwordEncoder.encode(signUpRequest.getPassword()));

                            return userRepository.save(user)
                                    .map(userMapper::toSignUpResponse);
                          }
                          return Mono.error(new SecurityException(HttpStatus.BAD_REQUEST,
                                  "This password does not meet the length, complexity, age, or history requirements of your corporate password policy."));
                        });
              }
              return Mono.error(new SecurityException(HttpStatus.BAD_REQUEST,"The email entered already exists"));
            })
            .onErrorResume(throwable -> {
              if (throwable instanceof SecurityException) {
                return Mono.error(throwable);
              }
              return Mono.error(new SecurityException(HttpStatus.INTERNAL_SERVER_ERROR,
                      "Error authentication user", throwable));
            });
  }
}

