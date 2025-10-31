package com.chat.server.infrastructure.repository.jpa.entity;

import java.util.UUID;

import com.chat.server.domain.enumerator.UserRoleEnum;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@Entity
@Table(name = "role")
public class RoleEntity {
  
  @Id
  private Long id;

  @Enumerated(EnumType.STRING)
  private UserRoleEnum name;

  private UUID keycloakId;

}
