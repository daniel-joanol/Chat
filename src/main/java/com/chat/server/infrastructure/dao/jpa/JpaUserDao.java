package com.chat.server.infrastructure.dao.jpa;

import java.util.UUID;

import org.springframework.stereotype.Component;

import com.chat.server.domain.dao.UserDao;
import com.chat.server.domain.model.User;
import com.chat.server.infrastructure.exception.EntityNotFoundException;
import com.chat.server.infrastructure.repository.jpa.UserJpaRepository;
import com.chat.server.infrastructure.repository.jpa.entity.UserEntity;
import com.chat.server.infrastructure.repository.jpa.mapper.UserEntityMapper;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JpaUserDao implements UserDao{
  
  private final UserJpaRepository repository;
  private final UserEntityMapper mapper;

  @Override
  public boolean existsByEmail(String email) {
    return repository.existsByEmail(email);
  }

  @Override
  public boolean existsByUsername(String username) {
    return repository.existsByUsername(username);
  }

  @Override
  public void delete(UUID id) {
    repository.deleteById(id);
  }

  @Override
  public User getById(UUID id) {
    return repository.findById(id)
        .map(mapper::toDomain)
        .orElseThrow(() -> {
          String message = String.format("User not found: %s", id);
          throw new EntityNotFoundException(message);
        });
  }
  
  @Override
  public User getByUsername(String username) {
    return repository.getByUsername(username)
        .map(mapper::toDomain)
        .orElseThrow(() -> {
          String message = String.format("Username not found: %s", username);
          throw new EntityNotFoundException(message);
        });
  }

  @Override
  public User save(User user) {
    UserEntity entity = mapper.toEntity(user);
    entity = repository.saveAndFlush(entity);
    return mapper.toDomain(entity);
  }

}
