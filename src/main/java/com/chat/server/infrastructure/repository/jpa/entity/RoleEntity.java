package com.chat.server.infrastructure.repository.jpa.entity;

import java.util.UUID;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class RoleEntity {
  
  @Id
  private Long id;

  @Enumerated(EnumType.STRING)
  private String name;

  private UUID keycloakId;

}
