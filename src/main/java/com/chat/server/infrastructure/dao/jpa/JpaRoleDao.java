package com.chat.server.infrastructure.dao.jpa;

import org.springframework.stereotype.Component;

import com.chat.server.domain.dao.RoleDao;
import com.chat.server.domain.enumerator.UserRoleEnum;
import com.chat.server.domain.model.Role;
import com.chat.server.infrastructure.exception.EntityNotFoundException;
import com.chat.server.infrastructure.repository.jpa.RoleJpaRepository;
import com.chat.server.infrastructure.repository.jpa.mapper.RoleEntityMapper;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class JpaRoleDao implements RoleDao {

  private final RoleJpaRepository repository;
  private final RoleEntityMapper mapper;
  
  @Override
  public Role getByName(UserRoleEnum name) {
    return repository.getByName(name)
        .map(mapper::toDomain)
        .orElseThrow(() -> {
          String internalMessage = String.format("Role not found: %s", name);
          throw new EntityNotFoundException(internalMessage);
        });
  }

}
