package com.chat.server.infrastructure.repository.http.request;

import java.util.UUID;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class KeycloakRoleRequest {
  
  private UUID id;
  private String name;
  private String description;
  private UUID clientId;

}
