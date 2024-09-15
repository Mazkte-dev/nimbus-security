package com.encora.samples.nimbus.security.auth.model.domain;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class Audit {

  private LocalDateTime createdDate;

  private LocalDateTime lastModifiedDate;

  private LocalDateTime lastLogin;

}
