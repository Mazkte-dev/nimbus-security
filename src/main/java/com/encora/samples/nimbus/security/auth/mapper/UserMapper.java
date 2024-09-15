package com.encora.samples.nimbus.security.auth.mapper;

import com.encora.samples.nimbus.security.auth.model.api.SignUpRequest;
import com.encora.samples.nimbus.security.auth.model.api.SignUpResponse;
import com.encora.samples.nimbus.security.auth.model.domain.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {


  @Mapping(expression = "java(java.time.LocalDateTime.now())" , target = "audit.createdDate")
  @Mapping(constant = "true", target = "preferences.dueSoonNotifications")
  @Mapping(constant = "true" , target = "preferences.overdueNotifications")
  @Mapping(source = "email" , target = "email")
  User toUser(SignUpRequest signUpRequest);

  @Mapping(source = "id" , target = "id")
  @Mapping(source = "audit.createdDate" , target = "createdDate")
  SignUpResponse toSignUpResponse(User user);

}
