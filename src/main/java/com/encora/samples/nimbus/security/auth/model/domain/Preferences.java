package com.encora.samples.nimbus.security.auth.model.domain;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document("users-preferences")
public class Preferences {

  private Boolean dueSoonNotifications;

  private Boolean overdueNotifications;

}
