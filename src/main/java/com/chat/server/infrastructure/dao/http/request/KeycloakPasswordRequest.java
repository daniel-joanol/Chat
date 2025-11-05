package com.chat.server.infrastructure.dao.http.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class KeycloakPasswordRequest {
  
  private final String type = "password";
  private final boolean temporary = false;
  private String value;
  
}
