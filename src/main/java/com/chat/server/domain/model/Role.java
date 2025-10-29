package com.chat.server.domain.model;

import java.util.UUID;

import com.chat.server.domain.enumerator.UserRoleEnum;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class Role {
  
  private Long id;
  private UserRoleEnum name;
  private UUID keycloakId;
  private UUID containerId;

}
