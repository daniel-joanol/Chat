package com.chat.server.infrastructure.repository.jpa;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.chat.server.infrastructure.repository.jpa.entity.UserEntity;

public interface UserJpaRepository extends JpaRepository<UserEntity, UUID> {
  
  Optional<UserEntity> getByUsername(String username);

  boolean existsByEmail(String email);

  boolean existsByUsername(String username);

}
