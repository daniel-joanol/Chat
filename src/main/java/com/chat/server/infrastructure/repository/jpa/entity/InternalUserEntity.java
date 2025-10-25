package com.chat.server.infrastructure.repository.jpa.entity;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;

import com.chat.server.domain.enumerator.UserStatusEnum;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@Entity
@Table(name = "internal_user")
public class InternalUserEntity {

  @Id
  @GeneratedValue(generator = "UUID")
  @UuidGenerator
  private UUID id;

  private String username;
  private String email;
  private ZonedDateTime createdAt;

  @Enumerated(EnumType.STRING)
  private UserStatusEnum status;

  @OneToMany(
      mappedBy = "user",
      fetch = FetchType.LAZY,
      orphanRemoval = true)
  private List<ContactEntity> contacts;
  
}
