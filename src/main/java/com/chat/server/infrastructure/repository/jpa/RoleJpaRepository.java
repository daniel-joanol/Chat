package com.chat.server.infrastructure.repository.jpa;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.chat.server.domain.enumerator.UserRoleEnum;
import com.chat.server.infrastructure.repository.jpa.entity.RoleEntity;

public interface RoleJpaRepository extends JpaRepository<RoleEntity, Long> {
  
  Optional<RoleEntity> getByName(UserRoleEnum name);
}
