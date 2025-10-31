package com.chat.server.infrastructure.dao.http.request;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class KeycloakUserRequest {
  
  private String email;
  private Boolean emailVerified;
  private Boolean enabled;
  private String firstName;
  private String lastName;
  private String username;

}
