package com.chat.server.infrastructure.dao.http.request;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class KeycloakRoleRequest {
  
  private UUID id;
  private String name;
  private final String description = "";

  @JsonIgnore
  private UUID containerId;

}
