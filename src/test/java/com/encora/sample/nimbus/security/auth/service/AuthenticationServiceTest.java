package com.encora.sample.nimbus.security.auth.service;


import com.encora.samples.nimbus.security.auth.exception.SecurityException;
import com.encora.samples.nimbus.security.auth.mapper.UserMapper;
import com.encora.samples.nimbus.security.auth.model.api.AuthenticationRequest;
import com.encora.samples.nimbus.security.auth.model.api.AuthenticationResponse;
import com.encora.samples.nimbus.security.auth.model.api.SignUpRequest;
import com.encora.samples.nimbus.security.auth.model.api.SignUpResponse;
import com.encora.samples.nimbus.security.auth.model.domain.User;
import com.encora.samples.nimbus.security.auth.repository.UserRepository;
import com.encora.samples.nimbus.security.auth.service.AuthenticationService;
import com.encora.samples.nimbus.security.auth.service.PasswordValidateService;
import com.encora.samples.nimbus.security.auth.utils.JwtUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

  @Mock
  private UserRepository userRepository;

  @Mock
  private PasswordEncoder passwordEncoder;

  @Mock
  private JwtUtils jwtUtils;

  @Mock
  private UserMapper userMapper;

  @Mock
  private PasswordValidateService passwordValidateService;

  @InjectMocks
  private AuthenticationService authenticationService;

  private User user;

  private AuthenticationRequest authenticationRequest;

  private SignUpRequest signUpRequest;

  @BeforeEach
  void setUp() {
    user = new User();
    user.setId("1");
    user.setEmail("test@example.com");
    user.setPasswordHash("hashedPassword");

    authenticationRequest = new AuthenticationRequest();
    authenticationRequest.setEmail("test@example.com");
    authenticationRequest.setPassword("password");

    signUpRequest = new SignUpRequest();
    signUpRequest.setEmail("test@example.com");
    signUpRequest.setPassword("password");
  }

  @Test
  void testAuthenticateUserSuccess() {
    when(userRepository.findByEmail(authenticationRequest.getEmail())).thenReturn(Mono.just(user));
    when(passwordEncoder.matches(authenticationRequest.getPassword(), user.getPasswordHash())).thenReturn(true);
    when(jwtUtils.generateJwtToken(user.getId())).thenReturn("jwtToken");

    Mono<AuthenticationResponse> result = authenticationService.authenticateUser(authenticationRequest);

    StepVerifier.create(result)
            .assertNext(response -> {
              assertEquals("jwtToken", response.getToken());
            })
            .verifyComplete();
  }

  @Test
  void testAuthenticateUserInvalidCredentials() {
    when(userRepository.findByEmail(authenticationRequest.getEmail())).thenReturn(Mono.just(user));
    when(passwordEncoder.matches(authenticationRequest.getPassword(), user.getPasswordHash())).thenReturn(false);

    Mono<AuthenticationResponse> result = authenticationService.authenticateUser(authenticationRequest);

    StepVerifier.create(result)
            .expectErrorMatches(throwable -> throwable instanceof SecurityException &&
                    throwable.getMessage().equals("Invalid username or password"))
            .verify();
  }

  @Test
  void testRegisterUserSuccess() {
    when(userRepository.findByEmail(signUpRequest.getEmail())).thenReturn(Mono.empty());
    when(passwordValidateService.validatePassword(signUpRequest.getPassword())).thenReturn(Mono.just(true));
    when(passwordEncoder.encode(signUpRequest.getPassword())).thenReturn("hashedPassword");
    when(userRepository.save(any(User.class))).thenReturn(Mono.just(user));
    when(userMapper.toSignUpResponse(any())).thenReturn(new SignUpResponse());
    when(userMapper.toUser(any())).thenReturn(new User());

    Mono<SignUpResponse> result = authenticationService.registerUser(signUpRequest);

    StepVerifier.create(result)
            .assertNext(response -> {
              assertNotNull(response);
            })
            .verifyComplete();
  }

  @Test
  void testRegisterUserEmailAlreadyExists() {
    when(userRepository.findByEmail(signUpRequest.getEmail())).thenReturn(Mono.just(user));

    Mono<SignUpResponse> result = authenticationService.registerUser(signUpRequest);

    StepVerifier.create(result)
            .expectErrorMatches(throwable -> throwable instanceof SecurityException &&
                    throwable.getMessage().equals("The email entered already exists"))
            .verify();
  }

  @Test
  void testRegisterUserInvalidPassword() {
    when(userRepository.findByEmail(signUpRequest.getEmail())).thenReturn(Mono.empty());
    when(passwordValidateService.validatePassword(signUpRequest.getPassword())).thenReturn(Mono.just(false));

    Mono<SignUpResponse> result = authenticationService.registerUser(signUpRequest);

    StepVerifier.create(result)
            .expectErrorMatches(throwable -> throwable instanceof SecurityException &&
                    throwable.getMessage().equals("This password does not meet the length, complexity, age, or history requirements of your corporate password policy."))
            .verify();
  }

  @Test
  void testAuthenticateUserException() {
    when(userRepository.findByEmail(authenticationRequest.getEmail())).thenReturn(Mono.error(new RuntimeException("Database error")));

    Mono<AuthenticationResponse> result = authenticationService.authenticateUser(authenticationRequest);

    StepVerifier.create(result)
            .expectErrorMatches(throwable -> throwable instanceof SecurityException &&
                    throwable.getMessage().equals("Error authentication user") &&
                    ((SecurityException) throwable).getStatus().equals(HttpStatus.INTERNAL_SERVER_ERROR))
            .verify();
  }

  @Test
  void testRegisterUserException() {
    when(userRepository.findByEmail(signUpRequest.getEmail())).thenReturn(Mono.error(new RuntimeException("Database error")));

    Mono<SignUpResponse> result = authenticationService.registerUser(signUpRequest);

    StepVerifier.create(result)
            .expectErrorMatches(throwable -> throwable instanceof SecurityException &&
                    throwable.getMessage().equals("Error authentication user") &&
                    ((SecurityException) throwable).getStatus().equals(HttpStatus.INTERNAL_SERVER_ERROR))
            .verify();
  }
}

