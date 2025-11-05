package com.chat.server.infrastructure.dao.jpa;

import org.springframework.stereotype.Component;

import com.chat.server.domain.dao.PropertyDao;
import com.chat.server.infrastructure.exception.InternalException;
import com.chat.server.infrastructure.repository.jpa.PropertyJpaRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JpaPropertyDao implements PropertyDao {
  
  private final PropertyJpaRepository repository;

  @Override
  public String getValueByName(String name) {
    return repository.getValueByName(name)
        .orElseThrow(() -> {
          String message = String.format("Property not found: %s", name);
          throw new InternalException(message);
        });
  }

}
