package com.encora.samples.nimbus.security.auth.model.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document("users")
public class User {

  @Id
  private String id;

  private String email;

  private String username;

  private String passwordHash;

  private Preferences preferences;

  private Audit audit;

}