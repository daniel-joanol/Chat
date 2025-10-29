package com.chat.server.infrastructure.repository.http.request;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class KeycloakPasswordRequest {
  
  private char[] value;
  private String type;
  private Boolean temporary;
  
}
