package com.chat.server.infrastructure.repository.jpa.entity;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;

import com.chat.server.domain.enumerator.UserStatusEnum;
import com.chat.server.domain.enumerator.UserTypeEnum;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@Entity
@Table(name = "_user")
public class UserEntity {

  @Id
  @GeneratedValue(generator = "UUID")
  @UuidGenerator
  private UUID id;

  private UUID keycloakId;
  private String username;
  private String firstName;
  private String lastName;
  private String email;
  private char[] password;
  private ZonedDateTime createdAt;
  private Boolean isEnabled;
  private Boolean isCreationCompleted;

  @Enumerated(EnumType.STRING)
  private UserStatusEnum status;

  @Enumerated(EnumType.STRING)
  private UserTypeEnum type;

  @ManyToOne
  @JoinColumn(name = "role_id")
  private RoleEntity role;

  @OneToMany(
      mappedBy = "user",
      fetch = FetchType.LAZY,
      orphanRemoval = true)
  private List<ContactEntity> contacts;
  
}
