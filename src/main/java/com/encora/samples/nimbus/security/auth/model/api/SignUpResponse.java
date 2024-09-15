package com.encora.samples.nimbus.security.auth.model.api;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class SignUpResponse {

  private String id;

  private LocalDateTime createdDate;

}
