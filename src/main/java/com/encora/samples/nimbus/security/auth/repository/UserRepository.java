package com.encora.samples.nimbus.security.auth.repository;


import com.encora.samples.nimbus.security.auth.model.domain.User;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface UserRepository extends ReactiveMongoRepository<User, String> {

  Mono<User> findByEmail(String email);
}